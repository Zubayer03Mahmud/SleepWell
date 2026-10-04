package com.example.sleepwell.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleepwell.data.model.ChatMessage
import com.example.sleepwell.data.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isTyping: Boolean = false,
    val inputText: String = "",
    val suggestedPrompts: List<String> = listOf(
        "How can I fall asleep faster?",
        "Why do I wake up tired?",
        "Is caffeine after 2 PM bad for sleep?",
        "How to build an ideal bedtime routine?"
    )
)

class ChatViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadHistoryOrInitial()
    }

    private fun loadHistoryOrInitial() {
        val userId = authRepository.getCurrentUserId()
        if (userId.isNullOrBlank()) {
            setInitialGreeting()
            return
        }

        viewModelScope.launch {
            try {
                val snapshot = firestore
                    .collection("users")
                    .document(userId)
                    .collection("chatMessages")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .get()
                    .await()

                if (!snapshot.isEmpty) {
                    val loaded = snapshot.documents.mapNotNull { doc ->
                        ChatMessage(
                            id = doc.getString("id") ?: doc.id,
                            text = doc.getString("text") ?: "",
                            isUser = doc.getBoolean("isUser") ?: false,
                            timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                        )
                    }
                    _uiState.value = _uiState.value.copy(messages = loaded)
                } else {
                    setInitialGreeting()
                }
            } catch (_: Exception) {
                setInitialGreeting()
            }
        }
    }

    private fun setInitialGreeting() {
        val greeting = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = "Hello! 👋 I'm your SleepWell AI Assistant. Ask me anything about sleep hygiene, bedtime routines, caffeine sensitivity, or how to boost your sleep score!",
            isUser = false,
            timestamp = System.currentTimeMillis()
        )
        _uiState.value = _uiState.value.copy(messages = listOf(greeting))
    }

    fun updateInputText(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun sendMessage(userText: String = _uiState.value.inputText) {
        val trimmed = userText.trim()
        if (trimmed.isEmpty()) return

        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = trimmed,
            isUser = true,
            timestamp = System.currentTimeMillis()
        )

        val updatedList = _uiState.value.messages + userMessage
        _uiState.value = _uiState.value.copy(
            messages = updatedList,
            inputText = "",
            isTyping = true
        )

        saveMessageToFirestore(userMessage)

        viewModelScope.launch {
            // Simulate AI thinking and response generation
            delay(1200)

            val aiReplyText = generateAIReply(trimmed)
            val aiMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                text = aiReplyText,
                isUser = false,
                timestamp = System.currentTimeMillis()
            )

            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + aiMessage,
                isTyping = false
            )

            saveMessageToFirestore(aiMessage)
        }
    }

    private fun generateAIReply(query: String): String {
        val lower = query.lowercase()
        return when {
            lower.contains("fall asleep") || lower.contains("insomnia") || lower.contains("can't sleep") -> {
                "Here are 4 evidence-based tips to fall asleep faster:\n\n" +
                        "1. 🧘 **4-7-8 Breathing**: Inhale for 4s, hold for 7s, exhale slowly for 8s.\n" +
                        "2. 📱 **Screen Off**: Avoid phone and TV screens at least 45-60 mins before bed.\n" +
                        "3. 🌡️ **Room Temp**: Keep bedroom cool (around 65°F-68°F / 18°C-20°C).\n" +
                        "4. 🧠 **Brain Dump**: Write down tomorrow's tasks so your mind stops looping."
            }
            lower.contains("tired") || lower.contains("fatigue") || lower.contains("wake up") -> {
                "Waking up tired often happens due to **sleep inertia** or fragmented REM sleep cycles.\n\n" +
                        "• **Hydrate immediately**: Drink 300ml of water right after waking up.\n" +
                        "• **Morning Sunlight**: Get 10-15 minutes of direct sunlight within 30 mins of waking.\n" +
                        "• **Sleep Cycles**: Try sleeping in multiples of 90-minute cycles (e.g. 7.5 hours)."
            }
            lower.contains("caffeine") || lower.contains("coffee") || lower.contains("tea") -> {
                "☕ Caffeine has a **half-life of 5 to 7 hours**! Drinking coffee at 3 PM means half of that caffeine is still active in your brain at 9 PM.\n\n" +
                        "💡 **Recommendation**: Set a caffeine cutoff time at **2:00 PM** to allow deep slow-wave sleep."
            }
            lower.contains("routine") || lower.contains("bedtime") || lower.contains("night") -> {
                "🌙 **Ideal 30-Minute Wind-Down Routine**:\n\n" +
                        "• **10:00 PM**: Turn off bright lights and devices.\n" +
                        "• **10:10 PM**: Warm shower or gentle stretching.\n" +
                        "• **10:20 PM**: Read a book or listen to calm music/white noise.\n" +
                        "• **10:30 PM**: Lights out in a dark, cool room."
            }
            lower.contains("stress") || lower.contains("anxiety") || lower.contains("mind") -> {
                "High cortisol (stress hormone) prevents your body from dropping core temperature for sleep.\n\n" +
                        "Try a 5-minute progressive muscle relaxation session or journal your thoughts before getting into bed."
            }
            else -> {
                "That's a great question about sleep health! To optimize your sleep quality:\n\n" +
                        "• Maintain a consistent sleep and wake schedule every day.\n" +
                        "• Avoid heavy meals and intense workouts within 2-3 hours of bed.\n" +
                        "• Ensure your room is dark, quiet, and cool.\n\n" +
                        "Feel free to ask me more about caffeine, stress management, or sleep cycles!"
            }
        }
    }

    private fun saveMessageToFirestore(message: ChatMessage) {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(userId)
                    .collection("chatMessages")
                    .document(message.id)
                    .set(
                        mapOf(
                            "id" to message.id,
                            "text" to message.text,
                            "isUser" to message.isUser,
                            "timestamp" to message.timestamp
                        )
                    ).await()
            } catch (e: Exception) {
                // Silently ignore if network issue
            }
        }
    }

    fun clearChat() {
        val userId = authRepository.getCurrentUserId()
        viewModelScope.launch {
            if (!userId.isNullOrBlank()) {
                try {
                    val snapshot = firestore.collection("users")
                        .document(userId)
                        .collection("chatMessages")
                        .get()
                        .await()

                    for (doc in snapshot.documents) {
                        doc.reference.delete().await()
                    }
                } catch (e: Exception) {
                    // Ignore
                }
            }
            setInitialGreeting()
        }
    }
}
