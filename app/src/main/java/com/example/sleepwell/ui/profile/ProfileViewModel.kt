package com.example.sleepwell.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleepwell.data.model.UserProfile
import com.example.sleepwell.data.repository.AuthRepository
import com.example.sleepwell.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: UserProfile? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isEditing: Boolean = false
)

class ProfileViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = userRepository.getUserProfile(userId)
            result.fold(
                onSuccess = { profile ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profile = profile ?: UserProfile(userId = userId, email = authRepository.getCurrentUser()?.email ?: "")
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to load profile: ${error.localizedMessage}"
                    )
                }
            )
        }
    }

    fun updateProfile(
        fullName: String,
        ageStr: String,
        gender: String,
        phoneNumber: String = "",
        bio: String = "",
        onComplete: (Boolean) -> Unit = {}
    ) {
        val userId = authRepository.getCurrentUserId() ?: return
        val ageInt = ageStr.toIntOrNull() ?: _uiState.value.profile?.age ?: 0

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            val updates = mapOf(
                "fullName" to fullName,
                "age" to ageInt,
                "gender" to gender,
                "phoneNumber" to phoneNumber,
                "bio" to bio,
                "updatedAt" to System.currentTimeMillis()
            )

            val result = userRepository.updateUserProfile(userId, updates)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isEditing = false,
                        profile = _uiState.value.profile?.copy(
                            fullName = fullName,
                            age = ageInt,
                            gender = gender,
                            phoneNumber = phoneNumber,
                            bio = bio,
                            updatedAt = System.currentTimeMillis()
                        ),
                        successMessage = "Profile updated successfully"
                    )
                    onComplete(true)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to update profile: ${error.localizedMessage}"
                    )
                    onComplete(false)
                }
            )
        }
    }

    fun uploadPhoto(uri: Uri) {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = userRepository.uploadProfilePhoto(userId, uri)
            result.fold(
                onSuccess = { url ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profile = _uiState.value.profile?.copy(profilePhotoUrl = url),
                        successMessage = "Profile photo updated!"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Photo upload failed: ${error.localizedMessage}"
                    )
                }
            )
        }
    }

    fun toggleEditing(editing: Boolean) {
        _uiState.value = _uiState.value.copy(isEditing = editing)
    }

    fun logout(onSuccess: () -> Unit) {
        authRepository.logout()
        onSuccess()
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }
}
