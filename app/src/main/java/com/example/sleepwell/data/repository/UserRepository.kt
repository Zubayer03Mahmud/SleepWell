package com.example.sleepwell.data.repository

import android.net.Uri
import com.example.sleepwell.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    private val usersCollection = firestore.collection("users")

    suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            val userDocRef = usersCollection.document(profile.userId)
            val profileMap = hashMapOf(
                "userId" to profile.userId,
                "fullName" to profile.fullName,
                "email" to profile.email,
                "gender" to profile.gender,
                "age" to profile.age,
                "dateOfBirth" to profile.dateOfBirth,
                "phoneNumber" to profile.phoneNumber,
                "profilePhotoUrl" to profile.profilePhotoUrl,
                "bio" to profile.bio,
                "createdAt" to profile.createdAt,
                "updatedAt" to profile.updatedAt,
                "targetSleepDuration" to profile.targetSleepDuration,
                "targetBedtime" to profile.targetBedtime,
                "targetSleepScore" to profile.targetSleepScore
            )
            userDocRef.set(profileMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(userId: String): Result<UserProfile?> {
        return try {
            val documentSnapshot = usersCollection.document(userId).get().await()
            if (documentSnapshot.exists()) {
                val profile = UserProfile(
                    userId = documentSnapshot.getString("userId") ?: userId,
                    fullName = documentSnapshot.getString("fullName") ?: "",
                    email = documentSnapshot.getString("email") ?: "",
                    gender = documentSnapshot.getString("gender") ?: "",
                    age = documentSnapshot.getLong("age")?.toInt() ?: 0,
                    dateOfBirth = documentSnapshot.getString("dateOfBirth") ?: "",
                    phoneNumber = documentSnapshot.getString("phoneNumber") ?: "",
                    profilePhotoUrl = documentSnapshot.getString("profilePhotoUrl") ?: "",
                    bio = documentSnapshot.getString("bio") ?: "",
                    createdAt = documentSnapshot.getLong("createdAt") ?: System.currentTimeMillis(),
                    updatedAt = documentSnapshot.getLong("updatedAt") ?: System.currentTimeMillis(),
                    targetSleepDuration = documentSnapshot.getDouble("targetSleepDuration") ?: 8.0,
                    targetBedtime = documentSnapshot.getString("targetBedtime") ?: "10:30 PM",
                    targetSleepScore = documentSnapshot.getLong("targetSleepScore")?.toInt() ?: 80
                )
                Result.success(profile)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            val mutableUpdates = updates.toMutableMap()
            mutableUpdates["updatedAt"] = System.currentTimeMillis()
            usersCollection.document(userId).update(mutableUpdates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadProfilePhoto(userId: String, imageUri: Uri): Result<String> {
        return try {
            val photoRef = storage.reference.child("profile_photos/$userId.jpg")
            photoRef.putFile(imageUri).await()
            val downloadUrl = photoRef.downloadUrl.await().toString()
            updateUserProfile(userId, mapOf("profilePhotoUrl" to downloadUrl))
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
