package org.ac.service.Usuarios

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.ac.APIConf.APIConf
import com.example.ac_a.APIRespuesta
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.encodeToString
import org.ac.Model.Usuarios.Profile
import org.ac.Model.Usuarios.Rol
import org.ac.Model.Usuarios.Usuario
import org.ac.service.Usuarios.interfaces.Usuarios
import java.io.File

class Usuarios(private val client:HttpClient):Usuarios {
    override suspend fun obtenerRol(): APIRespuesta<List<Rol>> {
        val response: HttpResponse = client.get(APIConf.ROLES_ENDPOINT) {
            contentType(ContentType.Application.Json)
        }
        val responseBody = response.bodyAsText()
        // Decodifica el JSON a APIRespuesta<List<Usuario>>
        return Json.decodeFromString<APIRespuesta<List<Rol>>>(responseBody)
    }

    override suspend fun obtenerUsuario(): APIRespuesta<List<Usuario>> {
        val response: HttpResponse = client.get(APIConf.USUARIOS_ENDPOINT) {
            contentType(ContentType.Application.Json)
        }
        val responseBody = response.bodyAsText()
        // Decodifica el JSON a APIRespuesta<List<Usuario>>
        return Json.decodeFromString<APIRespuesta<List<Usuario>>>(responseBody)
    }

    override suspend fun obtenerUsuarioId(usuarioId: String): APIRespuesta<Usuario> {
        val response: HttpResponse = client.get("${APIConf.USUARIOS_ENDPOINT}$usuarioId") {
            contentType(ContentType.Application.Json)
        }
        val responseBody = response.bodyAsText()
        return Json.decodeFromString<APIRespuesta<Usuario>>(responseBody)
    }

    override suspend fun crearUsuario(usuario: Usuario): APIRespuesta<Usuario> {
        return try {


            val response: HttpResponse = client.post(APIConf.PERFIL_ENDPOINT){
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(usuario))
            }
            val apiRespuesta = response.body<APIRespuesta<Usuario>>()
            apiRespuesta
        }catch (e: Exception){
            // Manejo de errores
            APIRespuesta(
                estado = false,
                mensaje = "Error al crear el usuario: ${e.message}",
                data = null
            )
        }
    }

    override suspend fun crearPerfil(perfil: Profile, imagen: File?): APIRespuesta<Profile> {
        return try {
            val formData = formData {
                append("carrera", perfil.carrera)
                append("id_user", perfil.id_user)
                append("telefono", perfil.telefono)
                append("direccion", perfil.direccion)
                append("numero_control", perfil.numero_control.toString())

                imagen?.let {
                    append("file", it.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, ContentType.Image.JPEG.toString())
                        append(HttpHeaders.ContentDisposition, "filename=\"${it.name}\"")
                    })
                }
            }
            // Previsualiza los datos antes de enviarlos
            /*
            formData.forEach { part ->
                println("Form part: ${part.key} = ${part.value}")
            }*/

            val response: HttpResponse = client.submitFormWithBinaryData(
                url = APIConf.PERFIL_ENDPOINT,
                formData = formData
            )
            // Procesar la respuesta desde el servidor
            val apiRespuesta = response.body<APIRespuesta<Profile>>()
            apiRespuesta
        } catch (e: Exception) {
            // Manejo de errores
            APIRespuesta(
                estado = false,
                mensaje = "Error al crear el perfil: ${e.message}",
                data = null
            )
        }
    }

    override suspend fun obtenerPerfil(usuarioId:String): APIRespuesta<Profile> {
        return try {
            val response: HttpResponse = client.get("${APIConf.PERFIL_ENDPOINT}$usuarioId") {
                contentType(ContentType.Application.Json)
            }

            if (response.status == HttpStatusCode.NotFound) {
                return APIRespuesta(
                    estado = false,
                    mensaje = "Usuario no encontrado",
                    data = null
                )
            }

            val responseBody = response.bodyAsText()
            Json.decodeFromString<APIRespuesta<Profile>>(responseBody)
        } catch (e: Exception) {
            // Manejo de excepciones
            println("Error al obtener el perfil: ${e.localizedMessage}")
            APIRespuesta(
                estado = false,
                mensaje = "Error al obtener el perfil: ${e.localizedMessage}",
                data = null
            )
        }
    }

}