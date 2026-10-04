package com.example.sleepwell.ui.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleepwell.data.model.SleepSession
import com.example.sleepwell.data.repository.AuthRepository
import com.example.sleepwell.data.repository.SleepRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SleepLogUiState(
    val sleepDuration: Float = 7.5f,
    val bedtime: String = "10:30 PM",
    val wakeTime: String = "06:00 AM",
    val stressLevel: Float = 4.0f,
    val screenTime: String = "1-2 hours",
    val activity: String = "Moderate (30 min)",
    val caffeine: String = "1 cup (morning)",
    val notes: String = "",
    val isLoading: Boolean = false,
    val currentSession: SleepSession? = null,
    val errorMessage: String? = null,
    val isSaved: Boolean = false
)

class SleepLogViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val sleepRepository: SleepRepository = SleepRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SleepLogUiState())
    val uiState: StateFlow<SleepLogUiState> = _uiState.asStateFlow()

    fun updateDuration(duration: Float) {
        _uiState.value = _uiState.value.copy(sleepDuration = duration)
    }

    fun updateBedtime(bedtime: String) {
        _uiState.value = _uiState.value.copy(bedtime = bedtime)
    }

    fun updateWakeTime(wakeTime: String) {
        _uiState.value = _uiState.value.copy(wakeTime = wakeTime)
    }

    fun updateStressLevel(stress: Float) {
        _uiState.value = _uiState.value.copy(stressLevel = stress)
    }

    fun updateScreenTime(screenTime: String) {
        _uiState.value = _uiState.value.copy(screenTime = screenTime)
    }

    fun updateActivity(activity: String) {
        _uiState.value = _uiState.value.copy(activity = activity)
    }

    fun updateCaffeine(caffeine: String) {
        _uiState.value = _uiState.value.copy(caffeine = caffeine)
    }

    fun updateNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }

    fun generatePrediction(): SleepSession {
        val state = _uiState.value
        val userId = authRepository.getCurrentUserId() ?: ""
        
        // Calculate score based on inputs
        val baseScore = 80
        val durationDelta = ((state.sleepDuration - 7f) * 4).toInt()
        val stressPenalty = ((state.stressLevel - 3f) * 2).toInt().coerceAtLeast(0)
        val score = (baseScore + durationDelta - stressPenalty).coerceIn(40, 100)

        val strengths = mutableListOf<String>()
        val improvements = mutableListOf<String>()

        if (state.sleepDuration >= 7f) strengths.add("Adequate sleep duration")
        if (state.stressLevel <= 4f) strengths.add("Low stress levels today")
        if (state.activity.contains("30") || state.activity.contains("Moderate") || state.activity.contains("High")) {
            strengths.add("Good physical activity")
        }

        if (state.screenTime.contains("2") || state.screenTime.contains("3") || state.screenTime.contains("More")) {
            improvements.add("High screen time before bed")
        }
        if (state.caffeine.contains("evening") || state.caffeine.contains("afternoon")) {
            improvements.add("Evening caffeine intake detected")
        }
        if (state.stressLevel > 6f) {
            improvements.add("Elevated stress level")
        }

        val category = when {
            score >= 85 -> "Excellent Sleep"
            score >= 70 -> "Good Sleep"
            score >= 55 -> "Fair Sleep"
            else -> "Poor Sleep"
        }

        val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val session = SleepSession(
            userId = userId,
            date = currentDate,
            timestamp = System.currentTimeMillis(),
            sleepDuration = state.sleepDuration,
            bedtime = state.bedtime,
            wakeTime = state.wakeTime,
            stressLevel = state.stressLevel,
            screenTime = state.screenTime,
            activity = state.activity,
            caffeine = state.caffeine,
            notes = state.notes,
            sleepScore = score,
            qualityCategory = category,
            aiConfidence = 94,
            strengths = strengths,
            improvements = improvements
        )

        _uiState.value = _uiState.value.copy(currentSession = session)
        return session
    }

    fun saveSession(onSuccess: () -> Unit) {
        val session = _uiState.value.currentSession ?: generatePrediction()
        val userId = authRepository.getCurrentUserId()

        if (userId.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "User not logged in.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = sleepRepository.saveSleepSession(userId, session)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
                    onSuccess()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to save sleep log: ${error.localizedMessage}"
                    )
                }
            )
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
