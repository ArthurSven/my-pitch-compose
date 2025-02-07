package com.devapps.mypitch.data.repository

import com.devapps.mypitch.data.model.Pitch
import com.devapps.mypitch.data.model.PitchResponse
import com.devapps.mypitch.ui.utils.state.Response
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.postgrest.postgrest

interface PitchRepository {

    suspend fun createPitch(pitch: Pitch) : Response

    suspend fun getPitches() : List<PitchResponse>

    suspend fun getPitchById(pitchid: String) : PitchResponse
}

class SupabaseRepository(private val supabaseClient: SupabaseClient) : PitchRepository {

    override suspend fun createPitch(pitch: Pitch): Response {
        return try {
            supabaseClient.postgrest["pitch"].insert(listOf(pitch))
            Response.Success
        } catch (e: SupabaseEncodingException) {
            val errorMessage = e.message ?: "Supabase Error: ${e.cause?.message}"  // More detailed error message
            Response.Error(Exception(errorMessage))
        } catch (e: Exception) {
            Response.Error(Exception("An unexpected error occurred: ${e.message}"))
        }
    }

    override suspend fun getPitches(): List<PitchResponse> {
        return try {
            val response = supabaseClient.postgrest["pitch"]
                .select()
                .decodeList<PitchResponse>()  // Decodes the result into a list of Pitch objects
            response
        } catch (e: SupabaseEncodingException) {
            emptyList()  // Return an empty list in case of decoding errors
        } catch (e: Exception) {
            emptyList()  // Handle unexpected errors
        }
    }

    override suspend fun getPitchById(pitchid: String): PitchResponse {
        return try {
            // Query the Supabase database for a pitch with the given pitchid
            val response = supabaseClient.postgrest["pitch"]
                .select {
                    filter {
                        eq("pitchid", pitchid) // Filter by pitchid
                    }
                }
                .decodeSingle<PitchResponse>() // Decode the result into a single PitchResponse object

            response
        } catch (e: SupabaseEncodingException) {
            // Handle decoding errors (e.g., invalid data format)
            throw Exception("Failed to decode pitch: ${e.message}")
        } catch (e: NoSuchElementException) {
            // Handle case where no pitch is found with the given ID
            throw Exception("Pitch with ID $pitchid not found")
        } catch (e: Exception) {
            // Handle other exceptions (e.g., network errors)
            throw Exception("Failed to fetch pitch: ${e.message}")
        }
    }

}