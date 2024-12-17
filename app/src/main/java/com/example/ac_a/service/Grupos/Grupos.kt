package com.example.ac_a.service.Grupos

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Grupos.Grupo
import com.example.ac_a.service.Grupos.interfaces.Grupos
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.ac.APIConf.APIConf

class GrupoServicio(private val client: HttpClient) : Grupos {
    override suspend fun obtenerGrupos(actividadId: Int): APIRespuesta<List<Grupo>> {
        return try {
            val response = client.get("${APIConf.GRUPOS_ENDPOINT}?actividad_id=$actividadId")
            val responseBody = response.bodyAsText()
            Json.decodeFromString(responseBody)
        } catch (e: Exception) {
            APIRespuesta(
                estado = false,
                mensaje = "Error al obtener grupos: ${e.message}",
                data = null
            )
        }
    }
}
