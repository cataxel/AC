package com.example.ac_a.Controller.Grupos

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Grupos.Grupo
import com.example.ac_a.service.Grupos.interfaces.Grupos

class GruposController(private val servicio: Grupos) {
    suspend fun obtenerGrupos(actividadId: Int): APIRespuesta<List<Grupo>> {
        return servicio.obtenerGrupos(actividadId)
    }
}
