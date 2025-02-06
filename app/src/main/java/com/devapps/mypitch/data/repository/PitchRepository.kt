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

}