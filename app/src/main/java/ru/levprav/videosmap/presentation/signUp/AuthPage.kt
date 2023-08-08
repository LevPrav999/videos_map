package ru.levprav.videosmap.presentation.signUp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthPage(navController: NavController, viewModel: AuthViewModel) {
    var dialogIndex by remember { mutableStateOf(1) } // 0 - choice 1 - signUp 2 - signIn

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }


    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.state.error) {
        viewModel.state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    when(dialogIndex){
        0 -> {}
        1 -> SignUp(
            email = email,
            password = password,
            passwordConfirm = passwordConfirm,
            onEmailChanged = {
                updatedValue -> email = updatedValue

        },onPasswordChanged = {
                    updatedValue -> password = updatedValue

            },onPasswordConfirmChanged = {
                    updatedValue -> passwordConfirm = updatedValue

            }, onBackPressed = {
                dialogIndex = 2
        }, onButtonPressed = {
            viewModel.signUp(email, password)
            keyboardController?.hide()
        })
        2 -> SignIn(
            email = email,
            password = password,
            onEmailChanged = {
                    updatedValue -> email = updatedValue

            },onPasswordChanged = { updatedValue ->
                password = updatedValue
            },
            onBackPressed = {
                dialogIndex = 1
            }, onButtonPressed = {
                viewModel.signIn(email, password)
                keyboardController?.hide()
            })
    }


    SnackbarHost(snackbarHostState)
}

@Composable
fun SignUp(
    email: String, password: String, passwordConfirm: String,
    onEmailChanged: (String) -> Unit, onPasswordChanged: (String) -> Unit, onPasswordConfirmChanged: (String) -> Unit, onBackPressed: () -> Unit, onButtonPressed: () -> Unit){

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
                    Text("Sign Up")
                }
            }
        }
    }
}

@Composable
fun SignIn(
    email: String, password: String,
    onEmailChanged: (String) -> Unit, onPasswordChanged: (String) -> Unit, onBackPressed: () -> Unit, onButtonPressed: () -> Unit){

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
                    Text("Sign In")
                }
            }
        }
    }
}