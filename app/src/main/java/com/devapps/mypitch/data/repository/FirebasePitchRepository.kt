package com.devapps.mypitch.data.repository

import android.util.Log
import com.devapps.mypitch.data.model.Pitch
import com.devapps.mypitch.data.model.PitchResponse
import com.devapps.mypitch.ui.utils.state.Response
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.util.UUID


class FirebasePitchRepository(firestore: FirebaseFirestore) : PitchRepository {

    private val pitchCollection = firestore.collection("pitches")


    override suspend fun createPitch(pitch: Pitch): Response {
        return try {
            val pitchId = UUID.randomUUID().toString()

            pitchCollection.document(pitchId).set(
                mapOf(
                    "pitchid" to pitchId,
                    "pitchname" to pitch.pitchname,
                    "category" to pitch.category,
                    "description" to pitch.description,
                    "google_id" to pitch.google_id,
                    "username" to pitch.username,
                    "email" to pitch.email,
                    "created_at" to com.google.firebase.Timestamp.now()
                )
            ).await()
            Response.Success
        } catch (e: Exception) {
           return Response.Error(Exception("Pitch creation failed: ${e.message}"))
        }
    }

    override suspend fun getPitches(): List<PitchResponse> {
        return try {
            pitchCollection
                .orderBy("created_at", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .map { document ->
                    PitchResponse(
                        pitchid = document.id, // oder document.getString("pitchid") wenn als Feld gespeichert
                        pitchname = document.getString("pitchname") ?: "",
                        category = document.getString("category") ?: "",
                        description = document.getString("description") ?: "",
                        google_id = document.getString("google_id"),
                        username = document.getString("username"),
                        email = document.getString("email")
                    )
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getPitchById(pitchid: String): PitchResponse {
        return try {
            val document = pitchCollection.document(pitchid).get().await()

            Log.d("TAG", document.toString())
            Log.d("DATA RECEIVED",  document.data.toString())

            val data = document.data ?: throw Exception("Document data is null")
            PitchResponse(
                pitchid = data["pitchid"] as? String ?: throw Exception("Missing pitchid"),
                pitchname = data["pitchname"] as? String ?: throw Exception("Missing pitchname"),
                category = data["category"] as? String ?: throw Exception("Missing category"),
                description = data["description"] as? String ?: throw Exception("Missing description"),
                google_id = data["google_id"] as? String,
                username = data["username"] as? String,
                email = data["email"] as? String
            )
        } catch (e: Exception) {
            throw Exception("Failed to fetch pitch: ${e.message}")
        }
    }

    override suspend fun getPitchesByUserId(userid: String): List<PitchResponse> {
        return try {
            pitchCollection
                .whereEqualTo("google_id", userid)
                .orderBy("created_at", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    val data = document.data ?: throw Exception("Document data is null")
                    PitchResponse(
                        pitchid = data["pitchid"] as? String ?: document.id, // Fallback to document ID if pitchid missing
                        pitchname = data["pitchname"] as? String ?: throw Exception("Missing pitchname"),
                        category = data["category"] as? String ?: throw Exception("Missing category"),
                        description = data["description"] as? String ?: throw Exception("Missing description"),
                        google_id = data["google_id"] as? String,
                        username = data["username"] as? String,
                        email = data["email"] as? String
                    )
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun deletePitch(pitchid: String): Response {
        return try {
            pitchCollection.document(pitchid).delete().await()
            Response.Success
        } catch (e: Exception) {
            Response.Error(Exception("Failed to delete pitch: ${e.message}"))
        }
    }

    override suspend fun updatePitch(pitch: Pitch, pitchid: String): Response {
        return try {
            val updates = mapOf(
                "pitchname" to pitch.pitchname,
                "category" to pitch.category,
                "description" to pitch.description,
                "google_id" to pitch.google_id,
                "username" to pitch.username,
                "email" to pitch.email
            )

            pitchCollection.document(pitchid).update(updates).await()
            Response.Success
        } catch (e: Exception) {
            Response.Error(Exception("Failed to update pitch: ${e.message}"))
        }
    }
}