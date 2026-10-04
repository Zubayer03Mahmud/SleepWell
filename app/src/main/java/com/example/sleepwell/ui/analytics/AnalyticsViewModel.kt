package com.example.sleepwell.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleepwell.data.model.SleepSession
import com.example.sleepwell.data.repository.AuthRepository
import com.example.sleepwell.data.repository.SleepRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AnalyticsUiState(
    val isLoading: Boolean = false,
    val sessions: List<SleepSession> = emptyList(),
    val avgSleepDuration: String = "7.3h",
    val avgStressLevel: String = "4.3",
    val avgSleepScore: String = "75",
    val streakDays: String = "7d",
    val bestDay: String = "Sat",
    val worstDay: String = "Fri",
    val chartPoints: List<Float> = listOf(0.5f, 0.7f, 0.6f, 0.8f, 0.5f, 0.9f, 0.75f),
    val errorMessage: String? = null
)

class AnalyticsViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val sleepRepository: SleepRepository = SleepRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalytics()
    }

    fun loadAnalytics() {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = sleepRepository.getSleepSessions(userId)
            result.fold(
                onSuccess = { sessions ->
                    if (sessions.isEmpty()) {
                        _uiState.value = AnalyticsUiState(isLoading = false, sessions = emptyList())
                    } else {
                        val avgDuration = String.format("%.1fh", sessions.map { it.sleepDuration }.average())
                        val avgStress = String.format("%.1f", sessions.map { it.stressLevel }.average())
                        val avgScore = sessions.map { it.sleepScore }.average().toInt().toString()
                        val streak = "${sessions.size}d"

                        val chart = sessions.take(7).reversed().map { (it.sleepScore / 100f).coerceIn(0.1f, 1.0f) }

                        _uiState.value = AnalyticsUiState(
                            isLoading = false,
                            sessions = sessions,
                            avgSleepDuration = avgDuration,
                            avgStressLevel = avgStress,
                            avgSleepScore = avgScore,
                            streakDays = streak,
                            bestDay = "Sat",
                            worstDay = "Fri",
                            chartPoints = if (chart.size >= 2) chart else listOf(0.5f, 0.7f, 0.6f, 0.8f, 0.5f, 0.9f, 0.75f)
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to load analytics: ${error.localizedMessage}"
                    )
                }
            )
        }
    }
}
