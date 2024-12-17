package com.example.ac_a.Model.Grupos

import kotlinx.serialization.Serializable

@Serializable
data class Grupo(
    val id: Int,
    val guid: String,
    val descripcion: String,
    val ubicacion: String,
    val hora_inicial: String,
    val hora_final: String,
    val fecha_inicial: String,
    val fecha_final: String,
    val capacidad: Int,
    val usuario_id: Int,
    val actividad_id: Int
)
