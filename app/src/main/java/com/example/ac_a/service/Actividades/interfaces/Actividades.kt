package com.example.ac_a.service.Actividades.interfaces

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Actividades.Actividad

interface Actividades {
    suspend fun obtenerActividad(): APIRespuesta<List<Actividad>>
    suspend fun crearActividad(actividad: Actividad): APIRespuesta<Actividad>
    suspend fun actualizarActividad(actividad: Actividad): APIRespuesta<Actividad>
    suspend fun eliminarActividad(actividad: Actividad): APIRespuesta<Actividad>
}
