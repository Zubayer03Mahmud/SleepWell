package com.example.sleepwell.data.model

data class SleepSession(
    val sessionId: String = "",
    val userId: String = "",
    val date: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val sleepDuration: Float = 7.0f,
    val bedtime: String = "10:30 PM",
    val wakeTime: String = "06:00 AM",
    val stressLevel: Float = 4.0f,
    val screenTime: String = "1-2 hours",
    val activity: String = "Moderate (30 min)",
    val caffeine: String = "1 cup (morning)",
    val notes: String = "",
    val sleepScore: Int = 82,
    val qualityCategory: String = "Excellent Sleep",
    val aiConfidence: Int = 94,
    val strengths: List<String> = emptyList(),
    val improvements: List<String> = emptyList()
)
