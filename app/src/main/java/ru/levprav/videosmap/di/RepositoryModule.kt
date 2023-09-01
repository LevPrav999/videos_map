package ru.levprav.videosmap.di


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.levprav.videosmap.data.repository.CommentRepositoryImpl
import ru.levprav.videosmap.data.repository.UserRepositoryImpl
import ru.levprav.videosmap.data.repository.VideoRepositoryImpl
import ru.levprav.videosmap.domain.repository.CommentRepository
import ru.levprav.videosmap.domain.repository.UserRepository
import ru.levprav.videosmap.domain.repository.VideoRepository
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindVideoRepository(
        videoRepositoryImpl: VideoRepositoryImpl
    ): VideoRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(
        commentRepositoryImpl: CommentRepositoryImpl
    ): CommentRepository
}