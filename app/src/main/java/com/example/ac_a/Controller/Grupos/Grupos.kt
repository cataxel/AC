package com.example.ac_a.Controller.Grupos

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Grupos.Grupo
import com.example.ac_a.service.Grupos.GrupoServicio

class GruposController(private val gruposService: GrupoServicio) {
    suspend fun obtenerGrupos(): APIRespuesta<List<Grupo>> {
        return try {
            gruposService.obtenerGrupo()
        } catch (e: Exception) {
            APIRespuesta(
                estado = false,
                mensaje = "Error al obtener grupos: ${e.message}",
                data = null
            )
        }
    }
}
