package com.example.ac_a.service.Actividades

import android.util.Log
import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Actividades.Actividad
import com.example.ac_a.Model.Actividades.ActividadResponse
import com.example.ac_a.Model.Inscripciones.Inscripcion
import com.example.ac_a.service.Actividades.interfaces.Actividades
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
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
        return try {
            val formData = formData {
                append("nombre", actividad.nombre)
                append("descripcion", actividad.descripcion)
            }

            val response: HttpResponse = client.submitFormWithBinaryData(
                url = APIConf.ACTIVIDADES_ENDPOINT,
                formData = formData,
            )

            val apiRespuesta = response.body<APIRespuesta<Actividad>>()
            apiRespuesta

        }catch (e: Exception){
            // Manejo de errores
            APIRespuesta(
                estado = false,
                mensaje = "Error al crear la actividad: ${e.message}",
                data = null
            )
        }
    }

    override suspend fun actualizarActividad(actividad: Actividad): APIRespuesta<Actividad> {
        return try {
            // Crear los datos del formulario
            val actividadData = mapOf(
                "nombre" to actividad.nombre,
                "descripcion" to actividad.descripcion
            )

            // Realizar la solicitud PUT
            val response: HttpResponse = client.put(APIConf.ACTIVIDADES_ENDPOINT+actividad.guid+"/") {
                contentType(ContentType.Application.Json)
                setBody(actividadData) // Enviar los datos como JSON
            }

            // Procesar la respuesta
            if (response.status.value in 200..299) {
                val apiRespuesta = response.body<APIRespuesta<Actividad>>()
                apiRespuesta
            } else {
                APIRespuesta(
                    estado = false,
                    mensaje = "Error al actualizar la actividad: ${response.status}",
                    data = null
                )
            }
        } catch (e: Exception) {
            // Manejo de errores
            APIRespuesta(
                estado = false,
                mensaje = "Error al realizar la solicitud PUT: ${e.message}",
                data = null
            )
        }
    }


    override suspend fun eliminarActividad(actividad: Actividad): APIRespuesta<Actividad> {
        return try {
            // Realiza la solicitud DELETE
            val response: HttpResponse = client.delete(APIConf.ACTIVIDADES_ENDPOINT+actividad.guid+"/") {
                contentType(ContentType.Application.Json)
            }
            // Verifica si la respuesta es exitosa
            if (response.status.value in 200..299) {
                APIRespuesta(
                    estado = true,
                    mensaje = "Actividad eliminada correctamente",
                    data = null
                )
            } else {
                APIRespuesta(
                    estado = false,
                    mensaje = "Error al eliminar la actividad: ${response.status}",
                    data = null
                )
            }
        } catch (e: Exception) {
            // Manejo de errores
            APIRespuesta(
                estado = false,
                mensaje = "Error al realizar la solicitud DELETE: ${e.message}",
                data = null
            )
        }
    }

}

