package com.example.ac_a

import kotlinx.serialization.Serializable

@Serializable
data class APIRespuesta<T>(
    val estado: Boolean,
    val mensaje: String,
    val data: T? = null
)