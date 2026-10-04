package com.example.sleepwell.data.model

data class UserProfile(
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val gender: String = "",
    val age: Int = 0,
    val dateOfBirth: String = "",
    val phoneNumber: String = "",
    val profilePhotoUrl: String = "",
    val bio: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val targetSleepDuration: Double = 8.0,
    val targetBedtime: String = "10:30 PM",
    val targetSleepScore: Int = 80
)
