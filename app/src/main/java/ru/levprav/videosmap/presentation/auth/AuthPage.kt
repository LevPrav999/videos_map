package ru.levprav.videosmap.presentation.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import ru.levprav.videosmap.presentation.auth.components.AuthDialog
import ru.levprav.videosmap.presentation.auth.components.ChoiceDialog

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthPage(viewModel: AuthViewModel) {
    var dialogIndex by remember { mutableIntStateOf(0) } // 0 - choice 1 - signUp 2 - signIn


    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.state) {
        viewModel.state.error?.let { error ->
            if (!viewModel.state.isLoading) {
                snackbarHostState.showSnackbar(error)
            }
        }
    }


    when (dialogIndex) {
        0 -> ChoiceDialog(onSignInClick = { dialogIndex = 2 }, onSignUpClick = { dialogIndex = 1 })
        else -> AuthDialog(
            isLoading = viewModel.state.isLoading,
            isSignUp = dialogIndex == 1,
            email = viewModel.state.data?.email ?: "",
            password = viewModel.state.data?.password ?: "",
            passwordConfirm = viewModel.state.data?.passwordConfirm ?: "",
            onEmailChanged = { updatedValue ->
                viewModel.onEmailChanged(updatedValue)

            }, onPasswordChanged = { updatedValue ->
                viewModel.onPasswordChanged(updatedValue)

            }, onPasswordConfirmChanged = { updatedValue ->
                viewModel.onPasswordConfirmChanged(updatedValue)

            }, onBackPressed = {
                dialogIndex = 0
            }, onButtonPressed = {
                if (dialogIndex == 1) {
                    viewModel.state.data?.email?.let {
                        viewModel.state.data?.password?.let { it1 ->
                            viewModel.state.data?.passwordConfirm?.let { it2 ->
                                viewModel.signUp(
                                    it,
                                    it1, it2, context
                                )
                            }
                        }
                    }
                    keyboardController?.hide()
                } else {
                    viewModel.state.data?.email?.let {
                        viewModel.state.data?.password?.let { it1 ->
                            viewModel.signIn(
                                it,
                                it1, context
                            )
                        }
                    }
                    keyboardController?.hide()
                }
            })
    }


    SnackbarHost(snackbarHostState)
}