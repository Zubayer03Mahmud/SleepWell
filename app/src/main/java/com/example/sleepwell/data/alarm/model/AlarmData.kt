package com.example.sleepwell.data.alarm.model

import kotlinx.serialization.Serializable

@Serializable
data class AlarmData(
    val id: Int,
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean,
    val label: String = "Alarm"
)
