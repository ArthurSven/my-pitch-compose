package com.devapps.mypitch.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.devapps.mypitch.data.model.Pitch
import com.devapps.mypitch.data.model.UserData
import com.devapps.mypitch.data.repository.PitchRepository
import com.devapps.mypitch.ui.utils.state.CreatePitchUiState
import com.devapps.mypitch.ui.utils.state.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class PitchViewModel(
    private val pitchRepository: PitchRepository,
    private val userData: UserData
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreatePitchUiState>(CreatePitchUiState.Idle)
    val uiState: StateFlow<CreatePitchUiState> = _uiState.asStateFlow()

    private val _pitches = MutableStateFlow<List<Pitch>>(emptyList())
    val pitches : StateFlow<List<Pitch>> = _pitches.asStateFlow()


    var pitchName by mutableStateOf("")
        private set

    var pitchCategory by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    // Methods to update form fields
    fun updatePitchName(input: String) {
        pitchName = input
    }

    fun updatePitchCategory(input: String) {
        pitchCategory = input
    }

    fun updateDescription(input: String) {
        description = input
    }

    suspend fun createPitch() {

        try {
            val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

            _uiState.value = CreatePitchUiState.Loading
            val pitch = Pitch(
                pitchname = pitchName,
                category = pitchCategory,
                description = description,
                google_id = userData.userId,
                username = userData.username,
                email = userData.email
            )

            val result = pitchRepository.createPitch(pitch)

            when (result) {
                is Response.Success -> _uiState.value = CreatePitchUiState.Success
                is Response.Error -> _uiState.value = CreatePitchUiState.Error((result.error ?: "Failed to create pitch.").toString())
            }
        } catch (e: Exception) {
            _uiState.value = CreatePitchUiState.Error("Error creating pitch data: ${e.message}")
        }
    }

    suspend fun getPitches() {
        try {
            val fetchedPitches = pitchRepository.getPitches()
            _pitches.value = fetchedPitches
        } catch (e: Exception) {
            _uiState.value = CreatePitchUiState.Error("Error fetching pitches: ${e.message}")
        }
    }
}