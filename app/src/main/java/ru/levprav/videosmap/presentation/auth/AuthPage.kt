package ru.levprav.videosmap.presentation.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthPage(viewModel: AuthViewModel) {
    var dialogIndex by remember { mutableStateOf(0) } // 0 - choice 1 - signUp 2 - signIn


    val keyboardController = LocalSoftwareKeyboardController.current

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
                                    it1, it2
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
                                it1
                            )
                        }
                    }
                    keyboardController?.hide()
                }
            })
    }


    SnackbarHost(snackbarHostState)
}

@Composable
fun AuthDialog(
    isLoading: Boolean,
    isSignUp: Boolean,
    email: String,
    password: String,
    passwordConfirm: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordConfirmChanged: (String) -> Unit,
    onBackPressed: () -> Unit,
    onButtonPressed: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = email,
                onValueChange = {
                    onEmailChanged(it)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            TextField(
                value = password,
                onValueChange = { onPasswordChanged(it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            if (isSignUp) {
                TextField(
                    value = passwordConfirm,
                    onValueChange = { onPasswordConfirmChanged(it) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = { Text("Confirm password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onBackPressed,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Back")
                }

                Button(
                    onClick = onButtonPressed,
                    modifier = Modifier.padding(8.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text(if (isSignUp) "Sign Up" else "Sign In")
                    }
                }
            }
        }
    }
}

@Composable
fun ChoiceDialog(onSignInClick: () -> Unit, onSignUpClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to VideosMap",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(text = "Sign In", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSignUpClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
        ) {
            Text(text = "Sign Up", color = Color.White)
        }
    }
}