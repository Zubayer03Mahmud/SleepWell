package com.example.sleepwell.data.repository

import com.example.sleepwell.data.model.SleepSession
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.UUID

class SleepRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun saveSleepSession(userId: String, session: SleepSession): Result<String> {
        return try {
            val sessionId = if (session.sessionId.isBlank()) UUID.randomUUID().toString() else session.sessionId
            val sessionWithId = session.copy(sessionId = sessionId, userId = userId)
            
            val sessionDocRef = firestore
                .collection("users")
                .document(userId)
                .collection("sleepSessions")
                .document(sessionId)

            val sessionMap = hashMapOf(
                "sessionId" to sessionWithId.sessionId,
                "userId" to sessionWithId.userId,
                "date" to sessionWithId.date,
                "timestamp" to sessionWithId.timestamp,
                "sleepDuration" to sessionWithId.sleepDuration,
                "bedtime" to sessionWithId.bedtime,
                "wakeTime" to sessionWithId.wakeTime,
                "stressLevel" to sessionWithId.stressLevel,
                "screenTime" to sessionWithId.screenTime,
                "activity" to sessionWithId.activity,
                "caffeine" to sessionWithId.caffeine,
                "notes" to sessionWithId.notes,
                "sleepScore" to sessionWithId.sleepScore,
                "qualityCategory" to sessionWithId.qualityCategory,
                "aiConfidence" to sessionWithId.aiConfidence,
                "strengths" to sessionWithId.strengths,
                "improvements" to sessionWithId.improvements
            )

            sessionDocRef.set(sessionMap).await()
            Result.success(sessionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSleepSessions(userId: String): Result<List<SleepSession>> {
        return try {
            val querySnapshot = firestore
                .collection("users")
                .document(userId)
                .collection("sleepSessions")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val sessions = querySnapshot.documents.mapNotNull { doc ->
                SleepSession(
                    sessionId = doc.getString("sessionId") ?: doc.id,
                    userId = doc.getString("userId") ?: userId,
                    date = doc.getString("date") ?: "",
                    timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis(),
                    sleepDuration = doc.getDouble("sleepDuration")?.toFloat() ?: (doc.getLong("sleepDuration")?.toFloat() ?: 7.0f),
                    bedtime = doc.getString("bedtime") ?: "10:30 PM",
                    wakeTime = doc.getString("wakeTime") ?: "06:00 AM",
                    stressLevel = doc.getDouble("stressLevel")?.toFloat() ?: (doc.getLong("stressLevel")?.toFloat() ?: 4.0f),
                    screenTime = doc.getString("screenTime") ?: "1-2 hours",
                    activity = doc.getString("activity") ?: "Moderate (30 min)",
                    caffeine = doc.getString("caffeine") ?: "1 cup (morning)",
                    notes = doc.getString("notes") ?: "",
                    sleepScore = doc.getLong("sleepScore")?.toInt() ?: 82,
                    qualityCategory = doc.getString("qualityCategory") ?: "Excellent Sleep",
                    aiConfidence = doc.getLong("aiConfidence")?.toInt() ?: 94,
                    strengths = (doc.get("strengths") as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                    improvements = (doc.get("improvements") as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                )
            }
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLatestSleepSession(userId: String): Result<SleepSession?> {
        return try {
            val querySnapshot = firestore
                .collection("users")
                .document(userId)
                .collection("sleepSessions")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val doc = querySnapshot.documents[0]
                val session = SleepSession(
                    sessionId = doc.getString("sessionId") ?: doc.id,
                    userId = doc.getString("userId") ?: userId,
                    date = doc.getString("date") ?: "",
                    timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis(),
                    sleepDuration = doc.getDouble("sleepDuration")?.toFloat() ?: (doc.getLong("sleepDuration")?.toFloat() ?: 7.0f),
                    bedtime = doc.getString("bedtime") ?: "10:30 PM",
                    wakeTime = doc.getString("wakeTime") ?: "06:00 AM",
                    stressLevel = doc.getDouble("stressLevel")?.toFloat() ?: (doc.getLong("stressLevel")?.toFloat() ?: 4.0f),
                    screenTime = doc.getString("screenTime") ?: "1-2 hours",
                    activity = doc.getString("activity") ?: "Moderate (30 min)",
                    caffeine = doc.getString("caffeine") ?: "1 cup (morning)",
                    notes = doc.getString("notes") ?: "",
                    sleepScore = doc.getLong("sleepScore")?.toInt() ?: 82,
                    qualityCategory = doc.getString("qualityCategory") ?: "Excellent Sleep",
                    aiConfidence = doc.getLong("aiConfidence")?.toInt() ?: 94,
                    strengths = (doc.get("strengths") as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                    improvements = (doc.get("improvements") as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                )
                Result.success(session)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
