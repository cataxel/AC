package com.example.ac_a.Model.Images

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LoginImage(
    @SerialName("public_id") val publicId: String,
    @SerialName("url") val url: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("description") val description: String
)

@Serializable
data class CloudinaryResponse(
    @SerialName("estado") val estado: Boolean,
    @SerialName("mensaje") val mensaje: String,
    @SerialName("data") val data: List<LoginImage>
)