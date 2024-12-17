package com.example.ac_a.Model.Actividades

import kotlinx.serialization.Serializable

@Serializable
data class Actividad(
    val id: Int,
    val guid: String,
    val nombre: String,
    val descripcion: String
)

@Serializable
data class ActividadResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Actividad>
)
