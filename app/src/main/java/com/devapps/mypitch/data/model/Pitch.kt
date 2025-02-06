package com.devapps.mypitch.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Pitch(
    val pitchname: String,
    val category: String,
    val description: String,
    val google_id: String?,
    val username: String?,
    val email: String?
)
