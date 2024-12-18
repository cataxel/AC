package com.example.ac_a.Model.Inscripciones


import kotlinx.serialization.Serializable

@Serializable
data class Inscripcion(
    val id: Int,
    val guid: String,
    val usuario_nombre: String,
    val grupo_descripcion: String,
    val estado_label: String,
    val fecha_inscripcion: String,
    val estado: String,
)
