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
    val usuario_nombre: String,
    val actividad_descripcion: String
)


@Serializable
data class GrupoResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Grupo>
)