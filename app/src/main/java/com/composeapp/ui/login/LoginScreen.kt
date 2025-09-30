// ui/login/LoginScreen.kt
package com.composeapp.ui.login

import CustomTextFieldWithIcons
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.composeapp.R
import com.composeapp.data.local.TokenManager
import com.composeapp.ui.components.ButtonPrimary
import com.composeapp.ui.components.ToastHost
import com.composeapp.ui.components.rememberToastState
import com.composeapp.ui.components.showError
import com.composeapp.ui.components.showSuccess
import com.composeapp.ui.components.showWarning
import com.composeapp.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    fun validateCredentials(username: String, password: String): ValidationResult {
        return when {
            username.isEmpty() && password.isEmpty() -> ValidationResult(
                isValid = false,
                usernameError = "Username is required",
                passwordError = "Password is required"
            )
            username.isEmpty() -> ValidationResult(
                isValid = false,
                usernameError = "Username is required",
                passwordError = null
            )
            password.isEmpty() -> ValidationResult(
                isValid = false,
                usernameError = null,
                passwordError = "Password is required"
            )
            username.length < 3 -> ValidationResult(
                isValid = false,
                usernameError = "Username must be at least 3 characters",
                passwordError = null
            )
            password.length < 6 -> ValidationResult(
                isValid = false,
                usernameError = null,
                passwordError = "Password must be at least 6 characters"
            )
            else -> ValidationResult(
                isValid = true,
                usernameError = null,
                passwordError = null
            )
        }
    }

    fun login(username: String, password: String): Boolean {
        val validation = validateCredentials(username, password)
        if (validation.isValid) {
            tokenManager.saveToken("mock_token_${System.currentTimeMillis()}")
            return true
        }
        return false
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val usernameError: String?,
    val passwordError: String?
)

//@Composable
//fun LoginScreen(
//    navController: NavController,
//    viewModel: LoginViewModel = hiltViewModel()
//) {
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var usernameError by remember { mutableStateOf<String?>(null) }
//    var passwordError by remember { mutableStateOf<String?>(null) }
//    var isPasswordVisible by remember { mutableStateOf(false) }
//
//    val scrollState = rememberScrollState()
//    val focusManager = LocalFocusManager.current
//
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(scrollState)
//                .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 40.dp)
//                .imePadding(), // Important: Adds padding when keyboard appears
//            horizontalAlignment = Alignment.Start,
//            verticalArrangement = Arrangement.Top
//        ) {
//            Spacer(modifier = Modifier.height(120.dp))
//
//            // Header Section - Aligned to Start
//            Image(
//                painter = painterResource(id = R.drawable.ic_login),
//                contentDescription = "Login",
//                modifier = Modifier
//                    .size(250.dp)
//                    .padding(bottom = 16.dp)
//            )
//
//            Text(
//                text = "Login to Your Account",
//                style = MaterialTheme.typography.headlineMedium.copy(
//                    fontWeight = FontWeight.Bold
//                ),
//                color = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier.padding(bottom = 32.dp)
//            )
//
//            // Username Section
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.Start
//            ) {
//                Text(
//                    text = "Username",
//                    style = MaterialTheme.typography.titleMedium.copy(
//                        fontWeight = FontWeight.SemiBold
//                    ),
//                    color = MaterialTheme.colorScheme.onBackground,
//                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
//                )
//
//                // Username Field
//                CustomTextFieldWithIcons(
//                    value = username,
//                    onValueChange = {
//                        username = it
//                        usernameError = null
//                    },
//                    placeholder = "Enter username",
//                    isError = usernameError != null,
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // Username Error Message
//                if (usernameError != null) {
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = usernameError ?: "",
//                        color = MaterialTheme.colorScheme.error,
//                        style = MaterialTheme.typography.bodySmall,
//                        modifier = Modifier.padding(start = 4.dp)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // Password Section
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.Start
//            ) {
//                Text(
//                    text = "Password",
//                    style = MaterialTheme.typography.titleMedium.copy(
//                        fontWeight = FontWeight.SemiBold
//                    ),
//                    color = MaterialTheme.colorScheme.onBackground,
//                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
//                )
//
//                // Password Field
//                CustomTextFieldWithIcons(
//                    value = password,
//                    onValueChange = {
//                        password = it
//                        passwordError = null
//                    },
//                    placeholder = "Enter password",
//                    isPassword = true,
//                    isError = passwordError != null,
//                    showPassword = isPasswordVisible,
//                    onPasswordVisibilityToggle = { isPasswordVisible = !isPasswordVisible },
//                    modifier = Modifier.fillMaxWidth(),
//                    trailingIcon = {
//                        Icon(
//                            painter = painterResource(
//                                id = if (isPasswordVisible) R.drawable.ic_eye_open
//                                else R.drawable.ic_eye_close
//                            ),
//                            contentDescription = if (isPasswordVisible) "Hide password"
//                            else "Show password",
//                            tint = Color.Gray
//                        )
//                    }
//                )
//
//                // Password Error Message
//                if (passwordError != null) {
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = passwordError ?: "",
//                        color = MaterialTheme.colorScheme.error,
//                        style = MaterialTheme.typography.bodySmall,
//                        modifier = Modifier.padding(start = 4.dp)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // Login Button
//            ButtonPrimary(
//                text = "Sign In",
//                onClick = {
//                    val validation = viewModel.validateCredentials(username, password)
//                    if (validation.isValid) {
//                        if (viewModel.login(username, password)) {
//                            focusManager.clearFocus()
//                            navController.navigate(Screen.Home.route) {
//                                popUpTo(Screen.Login.route) { inclusive = true }
//                            }
//                        }
//                    } else {
//                        usernameError = validation.usernameError
//                        passwordError = validation.passwordError
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(40.dp))
//        }
//    }
//}

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val toastState = rememberToastState() // Add this
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 40.dp)
                    .imePadding(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(120.dp))

                // Header Section - Aligned to Start
                Image(
                    painter = painterResource(id = R.drawable.ic_login),
                    contentDescription = "Login",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Login to Your Account",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Username Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Username",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )

                    // Username Field
                    CustomTextFieldWithIcons(
                        value = username,
                        onValueChange = {
                            username = it
                            usernameError = null
                        },
                        placeholder = "Enter username",
                        isError = usernameError != null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Username Error Message
                    if (usernameError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = usernameError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Password Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )

                    // Password Field
                    CustomTextFieldWithIcons(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null
                        },
                        placeholder = "Enter password",
                        isPassword = true,
                        isError = passwordError != null,
                        showPassword = isPasswordVisible,
                        onPasswordVisibilityToggle = { isPasswordVisible = !isPasswordVisible },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(
                                    id = if (isPasswordVisible) R.drawable.ic_eye_open
                                    else R.drawable.ic_eye_close
                                ),
                                contentDescription = if (isPasswordVisible) "Hide password"
                                else "Show password",
                                tint = Color.Gray
                            )
                        }
                    )

                    // Password Error Message
                    if (passwordError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = passwordError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))


                // Login Button
                ButtonPrimary(
                    text = "Sign In",
                    onClick = {
                        val validation = viewModel.validateCredentials(username, password)
                        if (validation.isValid) {
                            if (viewModel.login(username, password)) {
                                focusManager.clearFocus()

                                // Show success toast
                                toastState.showSuccess("Login successful!")

                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            } else {
                                // Show error toast
                                toastState.showError(
                                    message = "Login failed. Please try again.",
                                    actionText = "RETRY"
                                )
                            }
                        } else {
                            usernameError = validation.usernameError
                            passwordError = validation.passwordError

                            // Show validation error toast
                            toastState.showWarning("Please fill in all required fields")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Add Toast Host
        ToastHost(toastState = toastState)
    }
}