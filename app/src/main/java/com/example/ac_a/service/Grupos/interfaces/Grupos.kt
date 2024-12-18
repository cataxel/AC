package com.example.ac_a.service.Grupos.interfaces

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Grupos.Grupo

interface Grupos {
    suspend fun obtenerGrupo(): APIRespuesta<List<Grupo>>
    suspend fun crearGrupo(grupo: Grupo): APIRespuesta<Grupo>
}
