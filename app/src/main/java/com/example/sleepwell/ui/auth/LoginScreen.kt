package com.example.sleepwell.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sleepwell.ui.onboarding.StarBackground
import com.example.sleepwell.ui.theme.*

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }

    val uiState by authViewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlue)
    ) {
        StarBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo
            Text(text = "🌙", fontSize = 64.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome back",
                color = TextWhite,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Sign in to your account",
                color = TextGray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Error or Success Banner
            uiState.errorMessage?.let { error ->
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Red.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            uiState.successMessage?.let { success ->
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Green.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = success,
                        color = Color.Green,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Email Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Email Address", color = TextWhite, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        authViewModel.clearMessages()
                    },
                    placeholder = { Text(text = "hello@sleepwell.ai", color = TextGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkPurple,
                        unfocusedContainerColor = DarkPurple,
                        focusedBorderColor = ButtonGradientStart,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Password Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Password", color = TextWhite, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        authViewModel.clearMessages()
                    },
                    placeholder = { Text(text = "********", color = TextGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkPurple,
                        unfocusedContainerColor = DarkPurple,
                        focusedBorderColor = ButtonGradientStart,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null, tint = TextGray)
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }

            // Forgot Password
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                TextButton(onClick = {
                    resetEmail = email
                    showForgotPasswordDialog = true
                }) {
                    Text(text = "Forgot Password?", color = ButtonGradientStart, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Button
            Button(
                onClick = {
                    authViewModel.login(email, password, onSuccess = onLoginClick)
                },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(ButtonGradientStart, ButtonGradientEnd)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = TextWhite)
                    } else {
                        Text(
                            text = "Sign In",
                            color = TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = TextGray.copy(alpha = 0.3f))
                Text(
                    text = "or continue with",
                    color = TextGray,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 12.sp
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = TextGray.copy(alpha = 0.3f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Google Button
            OutlinedButton(
                onClick = { /* Google Sign In */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = DarkPurple),
                border = BorderStroke(1.dp, TextGray.copy(alpha = 0.3f))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "G", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Continue with Google", color = TextWhite)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Register Link
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Don't have an account?", color = TextGray)
                TextButton(onClick = onRegisterClick) {
                    Text(text = "Register", color = ButtonGradientEnd, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Password Reset Dialog
        if (showForgotPasswordDialog) {
            AlertDialog(
                onDismissRequest = { showForgotPasswordDialog = false },
                title = { Text(text = "Reset Password", color = TextWhite, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(text = "Enter your email to receive a password reset link.", color = TextGray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = resetEmail,
                            onValueChange = { resetEmail = it },
                            placeholder = { Text("email@example.com", color = TextGray) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkPurple,
                                unfocusedContainerColor = DarkPurple,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        authViewModel.sendPasswordReset(resetEmail) { success ->
                            if (success) {
                                showForgotPasswordDialog = false
                            }
                        }
                    }) {
                        Text("Send Email", color = ButtonGradientStart)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showForgotPasswordDialog = false }) {
                        Text("Cancel", color = TextGray)
                    }
                },
                containerColor = DarkPurple,
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    SleepWellTheme {
        LoginScreen(
            onLoginClick = {},
            onRegisterClick = {},
            onForgotPasswordClick = {}
        )
    }
}
