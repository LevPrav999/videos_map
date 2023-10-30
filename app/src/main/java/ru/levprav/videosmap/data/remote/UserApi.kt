package ru.levprav.videosmap.data.remote

import android.content.Context
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.models.InputFile
import io.appwrite.models.Session
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Realtime
import io.appwrite.services.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import ru.levprav.videosmap.domain.models.UserModel
import ru.levprav.videosmap.domain.models.toMap
import ru.levprav.videosmap.domain.models.toUserModel
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserApi @Inject constructor() {


    lateinit var client: Client
    lateinit var account: Account
    lateinit var databases: Databases
    lateinit var realtime: Realtime
    lateinit var storage: Storage

    var userId: String? = null


    suspend fun init(context: Context) {
        client = Client(context)
            .setEndpoint("https://cloud.appwrite.io/v1")
            .setProject("653e848b21aa10fbf791")

        account = Account(client)
        databases = Databases(client)
        realtime = Realtime(client)
        storage = Storage(client)

        userId = getCurrentUserId()
    }


    suspend fun signUp(email: String, password: String) = withContext(Dispatchers.IO) {
        account.create(
            userId = ID.unique(),
            email,
            password,
        )
    }

    suspend fun signIn(email: String, password: String): Session =
        withContext(Dispatchers.IO) {
            return@withContext account.createEmailSession(
                email,
                password,
            )
        }


    suspend fun addUserDocument(user: UserModel) = withContext(Dispatchers.IO) {
        databases.createDocument("1", "users", user.id, user.toMap())
    }


    suspend fun updateUserDocument(user: UserModel) = withContext(Dispatchers.IO) {
        databases.updateDocument("1", "users", user.id, user.toMap())
    }

    suspend fun getUserDocumentById(uid: String): UserModel? = withContext(Dispatchers.IO) {
        try {
            val user = databases.getDocument("1", "users", uid)
            return@withContext user.data.toUserModel()
        } catch (e: Exception) {
            return@withContext null
        }

    }


    private var userFlow: MutableStateFlow<UserModel?> = MutableStateFlow(null)
    suspend fun getUserSnapshots(): Flow<UserModel?> {
        realtime.subscribe("databases.1.collections.users.documents.$userId") { event ->
            if (event.events.contains("databases.1.collections.users.documents.$userId.update")) {
                val userModel = (event.payload as Map<String, Any>).toUserModel()
                userFlow.value = userModel
            }
        }

        return userFlow.asStateFlow()
    }


    suspend fun getUserSnapshotsById(uid: String): UserModel =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                realtime.subscribe("databases.1.collections.users.documents.${uid}", callback = {
                    if (it.events.contains("databases.1.collections.users.documents.${uid}.update")) {
                        continuation.resume((it.payload as Map<String, Any>).toUserModel())
                    }
                }
                )
            }
        }

    suspend fun saveUserAvatar(image: String): String =
        withContext(Dispatchers.IO) {

            val file = storage.createFile(
                "avatars",
                userId!!,
                InputFile.fromPath(image.toString())
            )

            return@withContext "https://cloud.appwrite.io/v1/storage/buckets/avatars/files/${file.id}/view?project=653e848b21aa10fbf791&mode=admin"
        }


    suspend fun checkUserDocumentExists(uid: String): Boolean = withContext(Dispatchers.IO) {
        try {
            databases.getDocument("1", "users", uid)
            return@withContext true
        } catch (e: Exception) {
            return@withContext false
        }

    }


    suspend fun follow(targetUid: String) = withContext(Dispatchers.IO) {
        val targetUser = getUserDocumentById(targetUid)!!
        val user = getUserDocumentById(userId!!)!!

        targetUser.followers.toMutableList().add(userId!!)
        user.following.toMutableList().add(targetUid)


        databases.updateDocument("1", "users", targetUid, targetUser.toMap())
        databases.updateDocument("1", "users", userId!!, user.toMap())
    }

    suspend fun unfollow(targetUid: String) = withContext(Dispatchers.IO) {
        val targetUser = getUserDocumentById(targetUid)!!
        val user = getUserDocumentById(userId!!)!!

        targetUser.followers.toMutableList().remove(userId!!)
        user.following.toMutableList().remove(targetUid)


        databases.updateDocument("1", "users", targetUid, targetUser.toMap())
        databases.updateDocument("1", "users", userId!!, user.toMap())
    }

    suspend fun getFollowers(uid: String) = withContext(Dispatchers.IO) {
        val user = getUserDocumentById(uid)!!

        val list = mutableListOf<UserModel>()

        for (userId in user.followers) {
            val follower = getUserDocumentById(userId)!!
            list.add(follower)
        }
        return@withContext list

    }

    suspend fun getFollowings(uid: String) = withContext(Dispatchers.IO) {

        val users = databases.listDocuments("1", "users", listOf(Query.search("followers", uid)))

        val list = mutableListOf<UserModel>()

        for (doc in users.documents) {
            list.add(doc.data.toUserModel())
        }

        return@withContext list

    }

    private suspend fun getCurrentUserId(): String? {
        try {
            val user = account.get()
            return user.id
        } catch (e: Exception) {
            return null
        }
    }
}