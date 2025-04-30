package com.devapps.mypitch.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapps.mypitch.data.model.Pitch
import com.devapps.mypitch.data.model.PitchResponse
import com.devapps.mypitch.data.model.UserData
import com.devapps.mypitch.data.repository.PitchRepository
import com.devapps.mypitch.ui.utils.state.CreatePitchUiState
import com.devapps.mypitch.ui.utils.state.GetPitchByIdUiState
import com.devapps.mypitch.ui.utils.state.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _pitches = MutableStateFlow<List<PitchResponse>>(emptyList())
    val pitches : StateFlow<List<PitchResponse>> = _pitches.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _pitch = MutableStateFlow<GetPitchByIdUiState>(GetPitchByIdUiState.Idle)
    val pitch: StateFlow<GetPitchByIdUiState> = _pitch

    private val _createdBy = MutableStateFlow("")
    val createdBy: StateFlow<String> = _createdBy

    init {
        viewModelScope.launch {
            getPitchesByUserId(_createdBy.value)
        }
    }

    fun setCreatedBy(userId: String) {
        _createdBy.value = userId
        viewModelScope.launch {
            getPitchesByUserId(userId)
        }

    }


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
            _isLoading.value = true
            val fetchedPitches = pitchRepository.getPitches()
            _pitches.value = fetchedPitches
        } catch (e: Exception) {
            _uiState.value = CreatePitchUiState.Error("Error fetching pitches: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun getPitchesByUserId(userid: String) {
        try {
            _isLoading.value = true
            val findPitches = pitchRepository.getPitchesByUserId(userid)
            _pitches.value = findPitches
        } catch (e: Exception) {
            _uiState.value = CreatePitchUiState.Error("Error fetching your pitches: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun getPitchById(pitchid: String) {
        _pitch.value = GetPitchByIdUiState.Loading // Set state to Loading
        try {
            // Fetch the pitch from the repository
            val pitch = pitchRepository.getPitchById(pitchid)

            pitchName = pitch.pitchname
            pitchCategory = pitch.category
            description = pitch.description

            _pitch.value = GetPitchByIdUiState.Success(pitch) // Set state to Success
        } catch (e: Exception) {
            // Handle errors and set state to Error
            _pitch.value = GetPitchByIdUiState.Error("Failed to fetch pitch: ${e.message}")
        }
    }

    suspend fun updatePitch(pitch: Pitch, pitchid: String) {
        try {
            _uiState.value = CreatePitchUiState.Loading
            val result = pitchRepository.updatePitch(pitch,pitchid)

            when (result) {
                is  Response.Success -> {
                    _uiState.value = CreatePitchUiState.Success
                    // Refetch the pitch data to update the UI
                    getPitchById(pitchid)
                    viewModelScope.launch {
                        delay(2000) // Optional: Add a small delay to show the success message
                        _uiState.value = CreatePitchUiState.Idle
                    }
                }
                is Response.Error -> {
                    _uiState.value = CreatePitchUiState.Error((result.error ?: "Failed to create pitch.").toString())
                }
            }
        } catch (e: Exception) {
            _uiState.value = CreatePitchUiState.Error("Error creating pitch data: ${e.message}")
        }

    }

    suspend fun deletePitch(pitchid: String) {
        val result = pitchRepository.deletePitch(pitchid)

        when(result) {
            is Response.Success -> {
               _uiState.value = CreatePitchUiState.Success
            }

            is Response.Error -> {
                _uiState.value = CreatePitchUiState.Error("Error: " + result.error)
            }
        }
    }

    fun resetState() {
        viewModelScope.launch {
            // Reset all UI states
            _uiState.value = CreatePitchUiState.Idle

            // Reset form fields if needed
            pitchName = ""
            pitchCategory = ""
            description = ""
        }
    }
}