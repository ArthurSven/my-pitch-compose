package com.devapps.mypitch.ui.utils.state

import com.devapps.mypitch.data.model.Pitch
import com.devapps.mypitch.data.model.PitchResponse

sealed class CreatePitchUiState {
    object Idle : CreatePitchUiState()
    object Loading : CreatePitchUiState()
    object Success : CreatePitchUiState()
    data class Error(val message: String) : CreatePitchUiState()
}

sealed class Response {
    object Success : Response()
    class Error(val error: Exception) : Response()
}

sealed class GetPitchByIdUiState {
    object Idle : GetPitchByIdUiState() // Initial state, no action has been taken yet
    object Loading : GetPitchByIdUiState() // State when the pitch is being fetched
    data class Success(val pitch: PitchResponse) : GetPitchByIdUiState() // State when the pitch is successfully fetched
    data class Error(val message: String) : GetPitchByIdUiState() // State when an error occurs
}