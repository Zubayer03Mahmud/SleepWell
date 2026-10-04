package com.example.sleepwell.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleepwell.data.model.SleepSession
import com.example.sleepwell.data.model.UserProfile
import com.example.sleepwell.data.repository.AuthRepository
import com.example.sleepwell.data.repository.SleepRepository
import com.example.sleepwell.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
    val latestSession: SleepSession? = null,
    val recentSessions: List<SleepSession> = emptyList(),
    val averageScore: Int = 82,
    val trendPoints: List<Float> = listOf(0.6f, 0.4f, 0.7f, 0.5f, 0.8f, 0.6f, 0.9f),
    val errorMessage: String? = null
)

class HomeViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository(),
    private val sleepRepository: SleepRepository = SleepRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val profileResult = userRepository.getUserProfile(userId)
            val profile = profileResult.getOrNull()

            val sessionsResult = sleepRepository.getSleepSessions(userId)
            val sessions = sessionsResult.getOrDefault(emptyList())

            val latest = sessions.firstOrNull()
            val avgScore = if (sessions.isNotEmpty()) {
                sessions.map { it.sleepScore }.average().toInt()
            } else {
                82
            }

            val trend = if (sessions.size >= 2) {
                sessions.take(7).reversed().map { (it.sleepScore / 100f).coerceIn(0.1f, 1.0f) }
            } else {
                listOf(0.6f, 0.4f, 0.7f, 0.5f, 0.8f, 0.6f, 0.9f)
            }

            _uiState.value = HomeUiState(
                isLoading = false,
                userProfile = profile,
                latestSession = latest,
                recentSessions = sessions,
                averageScore = avgScore,
                trendPoints = trend
            )
        }
    }
}
