package com.devapps.mypitch.ui.utils.state

import com.devapps.mypitch.data.model.Pitch

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