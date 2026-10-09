package com.example.sleepwell.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleepwell.data.model.UserProfile
import com.example.sleepwell.data.repository.AuthRepository
import com.example.sleepwell.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isAuthenticated: Boolean = false
)

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AuthUiState(isAuthenticated = authRepository.isUserLoggedIn())
    )
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter both email and password.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.loginUser(email.trim(), password)
            result.fold(
                onSuccess = { user ->
                    _uiState.value = AuthUiState(
                        isLoading = false,
                        isAuthenticated = true,
                        successMessage = "Logged in successfully!"
                    )
                    onSuccess()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = parseAuthError(error)
                    )
                }
            )
        }
    }

    fun loginWithGoogleToken(idToken: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.signInWithGoogle(idToken)
            result.fold(
                onSuccess = { user ->
                    val existingProfileResult = userRepository.getUserProfile(user.uid)
                    val existingProfile = existingProfileResult.getOrNull()

                    if (existingProfile == null) {
                        val userProfile = UserProfile(
                            userId = user.uid,
                            fullName = user.displayName ?: "Google User",
                            email = user.email ?: "",
                            profilePhotoUrl = user.photoUrl?.toString() ?: "",
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                        userRepository.saveUserProfile(userProfile)
                    }

                    _uiState.value = AuthUiState(
                        isLoading = false,
                        isAuthenticated = true,
                        successMessage = "Signed in with Google!"
                    )
                    onSuccess()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = parseAuthError(error)
                    )
                }
            )
        }
    }

    fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        ageStr: String,
        gender: String,
        onSuccess: () -> Unit
    ) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Full Name, Email, and Password are required.")
            return
        }

        if (password != confirmPassword) {
            _uiState.value = _uiState.value.copy(errorMessage = "Passwords do not match.")
            return
        }

        if (password.length < 6) {
            _uiState.value = _uiState.value.copy(errorMessage = "Password must be at least 6 characters.")
            return
        }

        val ageInt = ageStr.toIntOrNull() ?: 0

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val authResult = authRepository.registerUser(email.trim(), password)
            authResult.fold(
                onSuccess = { user ->
                    val userProfile = UserProfile(
                        userId = user.uid,
                        fullName = fullName.trim(),
                        email = email.trim(),
                        gender = gender.trim(),
                        age = ageInt,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    userRepository.saveUserProfile(userProfile).fold(
                        onSuccess = {
                            _uiState.value = AuthUiState(
                                isLoading = false,
                                isAuthenticated = true,
                                successMessage = "Account created successfully!"
                            )
                            onSuccess()
                        },
                        onFailure = { profileErr ->
                            _uiState.value = AuthUiState(
                                isLoading = false,
                                isAuthenticated = true,
                                errorMessage = "Account created, but profile save failed: ${profileErr.localizedMessage}"
                            )
                            onSuccess()
                        }
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = parseAuthError(error)
                    )
                }
            )
        }
    }

    fun sendPasswordReset(email: String, onComplete: (Boolean) -> Unit) {
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter your email address for password reset.")
            onComplete(false)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.sendPasswordResetEmail(email.trim())
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Password reset email sent to $email"
                    )
                    onComplete(true)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Failed to send password reset email."
                    )
                    onComplete(false)
                }
            )
        }
    }

    fun logout(onSuccess: () -> Unit) {
        authRepository.logout()
        _uiState.value = AuthUiState(isAuthenticated = false)
        onSuccess()
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }

    fun setCustomErrorMessage(message: String) {
        _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
    }

    private fun parseAuthError(error: Throwable): String {
        val msg = error.localizedMessage ?: "An error occurred."
        return if (msg.contains("API key not valid", ignoreCase = true) || msg.contains("API_KEY_INVALID", ignoreCase = true)) {
            "Invalid Firebase API Key: Please download 'google-services.json' from your Firebase Console and place it into the 'app/' directory."
        } else {
            msg
        }
    }
}
