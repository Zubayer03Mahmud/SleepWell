package com.example.sleepwell.data.model

data class ChatMessage(
    val id: String = "",
    val text: String = "",
    val isUser: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
