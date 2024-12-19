package com.example.ac_a.Controller.Grupos

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Grupos.Grupo
import com.example.ac_a.service.Grupos.GrupoServicio
import com.example.ac_a.service.Grupos.GruposServicioRetrofit

class GruposController(private val gruposService: GruposServicioRetrofit /* GrupoServicio */) {
    suspend fun obtenerGrupos(): APIRespuesta<List<Grupo>> {
        return try {
            APIRespuesta(
                estado = true,
                mensaje = "Grupos obtenidos correctamente",
                data = gruposService.obtenerGrupo().results
            )
        } catch (e: Exception) {
            APIRespuesta(
                estado = false,
                mensaje = "Error al obtener grupos: ${e.message}",
                data = null
            )
        }
    }
}
