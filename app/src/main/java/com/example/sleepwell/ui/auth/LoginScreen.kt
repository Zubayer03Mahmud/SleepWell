package com.example.sleepwell.ui.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }

    val uiState by authViewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Google Sign-In Launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (!idToken.isNullOrBlank()) {
                    authViewModel.loginWithGoogleToken(idToken, onSuccess = onLoginClick)
                } else {
                    authViewModel.setCustomErrorMessage("Google Sign-In: No ID Token received. Ensure SHA-1 fingerprint is added in Firebase Console.")
                }
            } catch (e: ApiException) {
                val errorMsg = when (e.statusCode) {
                    10 -> "Google Sign-In Error (10): SHA-1 fingerprint is missing in Firebase Console. Add your debug SHA-1 to project settings."
                    12500 -> "Google Sign-In Error (12500): Enable Google Sign-In in Firebase Console > Authentication."
                    else -> "Google Sign-In Error: ${e.localizedMessage ?: "Status Code ${e.statusCode}"}"
                }
                authViewModel.setCustomErrorMessage(errorMsg)
            }
        } else {
            authViewModel.setCustomErrorMessage("Google Sign-In cancelled or failed.")
        }
    }

    val defaultWebClientId = remember(context) {
        val resId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
        if (resId != 0) {
            context.getString(resId)
        } else {
            "289832563522-3ahue0a2g3dod3j0flitpc63ap3g8apd.apps.googleusercontent.com"
        }
    }

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
                onClick = {
                    if (defaultWebClientId.isNotBlank()) {
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(defaultWebClientId)
                            .requestEmail()
                            .build()
                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        googleSignInClient.signOut().addOnCompleteListener {
                            googleSignInLauncher.launch(googleSignInClient.signInIntent)
                        }
                    } else {
                        // Fallback if client ID is missing in dummy config
                        onLoginClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = DarkPurple),
                border = BorderStroke(1.dp, TextGray.copy(alpha = 0.3f))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "G", color = Color(0xFF4285F4), fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Continue with Google", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Register Link
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Don't have an account?", color = TextGray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                TextButton(onClick = onRegisterClick) {
                    Text(
                        text = "Register",
                        color = ButtonGradientEnd,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
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
