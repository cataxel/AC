package com.example.ac_a.service.Grupos

import android.util.Log
import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Grupos.Grupo
import com.example.ac_a.Model.Grupos.GrupoResponse
import com.example.ac_a.Model.Grupos.GrupoRequest
import com.example.ac_a.service.Grupos.interfaces.Grupos
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
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
        return try {
            val grupoCreateRequest = GrupoRequest(
                usuario = grupo.usuario_nombre,
                actividad = grupo.actividad_descripcion,
                descripcion = grupo.descripcion,
                ubicacion = grupo.ubicacion,
                hora_inicial = grupo.hora_inicial,
                hora_final = grupo.hora_final,
                fecha_inicial = grupo.fecha_inicial,
                fecha_final = grupo.fecha_final,
                capacidad = grupo.capacidad
            )

            val response: HttpResponse = client.post(APIConf.GRUPOS_ENDPOINT) {
                contentType(ContentType.Application.Json)
                setBody(grupoCreateRequest)  // Ahora serializas la clase
            }

            val apiRespuesta = response.body<APIRespuesta<Grupo>>()
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

    override suspend fun actualizarGrupo(grupo: Grupo): APIRespuesta<Grupo> {
        return try {
            // Crear los datos del formulario
            val grupoUpdateRequest = GrupoRequest(
                usuario = grupo.usuario_nombre,
                actividad = grupo.actividad_descripcion,
                descripcion = grupo.descripcion,
                ubicacion = grupo.ubicacion,
                hora_inicial = grupo.hora_inicial,
                hora_final = grupo.hora_final,
                fecha_inicial = grupo.fecha_inicial,
                fecha_final = grupo.fecha_final,
                capacidad = grupo.capacidad
            )

            Log.d("Beto", "Datos del formulario: $grupoUpdateRequest")

            val response: HttpResponse = client.put(APIConf.GRUPOS_ENDPOINT + grupo.guid + "/") {
                contentType(ContentType.Application.Json)
                setBody(grupoUpdateRequest)  // Ahora serializas la clase
            }


            // Procesar la respuesta
            if (response.status.value in 200..299) {
                val apiRespuesta = response.body<APIRespuesta<Grupo>>()
                apiRespuesta
            } else {
                APIRespuesta(
                    estado = false,
                    mensaje = "Error al actualizar el grupo: ${response.status}",
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


    override suspend fun eliminarGrupo(grupo: Grupo): APIRespuesta<Grupo> {
        return try {
            // Realiza la solicitud DELETE
            val response: HttpResponse = client.delete(APIConf.GRUPOS_ENDPOINT+grupo.guid+"/") {
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
