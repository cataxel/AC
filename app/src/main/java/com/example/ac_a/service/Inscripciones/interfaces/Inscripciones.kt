package com.example.ac_a.service.Grupos.interfaces

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Inscripciones.Inscripcion

interface Inscripciones {
    suspend fun crearInscripcion(usuario: String, grupo: String): APIRespuesta<Inscripcion>
}
