package com.example.sleepwell.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sleepwell.ui.onboarding.StarBackground
import com.example.sleepwell.ui.theme.*

@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

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
                .systemBarsPadding()
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextWhite
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Create Account",
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Error Banner
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

            // Profile Photo Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(DarkPurple)
                        .clickable { /* Tap to add photo */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = ButtonGradientEnd.copy(alpha = 0.6f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Tap to add photo",
                    color = TextGray,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Full Name Field
            CustomTextField(
                label = "Full Name",
                value = name,
                onValueChange = {
                    name = it
                    authViewModel.clearMessages()
                },
                placeholder = "Alex Johnson"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email Address Field
            CustomTextField(
                label = "Email Address",
                value = email,
                onValueChange = {
                    email = it
                    authViewModel.clearMessages()
                },
                placeholder = "alex@example.com",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password Field
            CustomTextField(
                label = "Password",
                value = password,
                onValueChange = {
                    password = it
                    authViewModel.clearMessages()
                },
                placeholder = "Min. 6 characters",
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = TextGray
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Password Field
            CustomTextField(
                label = "Confirm Password",
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    authViewModel.clearMessages()
                },
                placeholder = "Repeat password",
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = TextGray
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Age & Gender Row
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    CustomTextField(
                        label = "Age",
                        value = age,
                        onValueChange = { age = it },
                        placeholder = "25",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    CustomTextField(
                        label = "Gender",
                        value = gender,
                        onValueChange = { gender = it },
                        placeholder = "Male"
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Create Account Button
            Button(
                onClick = {
                    authViewModel.register(
                        fullName = name,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        ageStr = age,
                        gender = gender,
                        onSuccess = onRegisterClick
                    )
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
                            text = "Create Account",
                            color = TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Terms and Privacy Notice
            val annotatedString = buildAnnotatedString {
                append("By creating an account you agree to our ")
                withStyle(style = SpanStyle(color = ButtonGradientStart)) {
                    append("Terms")
                }
                append(" and ")
                withStyle(style = SpanStyle(color = ButtonGradientStart)) {
                    append("Privacy Policy")
                }
            }
            Text(
                text = annotatedString,
                color = TextGray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = TextWhite, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder, color = TextGray) },
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
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            singleLine = true,
            keyboardOptions = keyboardOptions
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    SleepWellTheme {
        RegisterScreen(
            onRegisterClick = {},
            onBackClick = {}
        )
    }
}
