package com.example.ac_a.service.Grupos

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Grupos.Grupo
import com.example.ac_a.Model.Grupos.GrupoResponse
import com.example.ac_a.service.Grupos.interfaces.Grupos
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.ac.APIConf.APIConf

class GrupoServicio(private val client: HttpClient) : Grupos {
    override suspend fun obtenerGrupo(): APIRespuesta<List<Grupo>> {
        return try {
            val response: HttpResponse = client.get(APIConf.GRUPOS_ENDPOINT) {
                contentType(ContentType.Application.Json)
            }
            val responseBody = response.bodyAsText()
            val grupoResponse = Json.decodeFromString<GrupoResponse>(responseBody)
            APIRespuesta(
                estado = true,
                mensaje = "Actividades obtenidas correctamente",
                data = grupoResponse.results
            )
        } catch (e: Exception) {
            APIRespuesta(
                estado = false,
                mensaje = "Error al obtener actividades: ${e.message}",
                data = null
            )
        }
    }

    override suspend fun crearGrupo(grupo: Grupo): APIRespuesta<Grupo> {
        TODO("Not yet implemented")
    }
}
