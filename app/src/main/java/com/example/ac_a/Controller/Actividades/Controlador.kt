package com.example.ac_a.Controller.Actividades

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Actividades.Actividad
import com.example.ac_a.service.Actividades.ActividadServicio

class ActividadesController(private val actividadesService: ActividadServicio) {
    suspend fun obtenerActividades(): APIRespuesta<List<Actividad>> {
        return try {
            actividadesService.obtenerActividad()
        } catch (e: Exception) {
            APIRespuesta(
                estado = false,
                mensaje = "Error al obtener actividades: ${e.message}",
                data = null
            )
        }
    }
}
