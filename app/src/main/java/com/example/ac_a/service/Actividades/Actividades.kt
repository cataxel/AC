package com.example.ac_a.service.Actividades

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Actividades.Actividad
import com.example.ac_a.Model.Actividades.ActividadResponse
import com.example.ac_a.service.Actividades.interfaces.Actividades
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.ac.APIConf.APIConf

class ActividadServicio(private val client: HttpClient) : Actividades {
    override suspend fun obtenerActividad(): APIRespuesta<List<Actividad>> {
        return try {
            val response: HttpResponse = client.get(APIConf.ACTIVIDADES_ENDPOINT) {
                contentType(ContentType.Application.Json)
            }
            val responseBody = response.bodyAsText()
            val actividadResponse = Json.decodeFromString<ActividadResponse>(responseBody)
            APIRespuesta(
                estado = true,
                mensaje = "Actividades obtenidas correctamente",
                data = actividadResponse.results
            )
        } catch (e: Exception) {
            APIRespuesta(
                estado = false,
                mensaje = "Error al obtener actividades: ${e.message}",
                data = null
            )
        }
    }

    override suspend fun crearActividad(actividad: Actividad): APIRespuesta<Actividad> {
        // Implementar el método para crear una actividad
        TODO()
    }
}
