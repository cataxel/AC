package org.ac.service.Usuarios

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.get
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
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import org.ac.Model.Usuarios.Profile
import org.ac.Model.Usuarios.ProfileModificar
import org.ac.Model.Usuarios.ProfileRespuesta
import org.ac.Model.Usuarios.Rol
import org.ac.Model.Usuarios.Usuario
import org.ac.Model.Usuarios.UsuarioRespuesta
import org.ac.service.Usuarios.interfaces.UsuarioApi
import org.ac.service.Usuarios.interfaces.Usuarios

class Usuarios(private val client:HttpClient):Usuarios {
    override suspend fun obtenerRol(): APIRespuesta<List<Rol>> {
        val response: HttpResponse = client.get(APIConf.ROLES_ENDPOINT) {
            contentType(ContentType.Application.Json)
        }
        val responseBody = response.bodyAsText()
        // Decodificar el JSON de la respuesta como un JsonObject
        val jsonObject = Json.parseToJsonElement(responseBody).jsonObject
        // Extraer el campo `results` del JSON
        val resultsJson = jsonObject["results"] ?: throw IllegalStateException("No se encontró el campo 'results'")
        // Deserializar `results` como una lista de roles
        val roles = Json.decodeFromJsonElement<List<Rol>>(resultsJson)
        // Crear manualmente un objeto APIRespuesta con los datos extraídos
        return APIRespuesta(
            estado = true, // Asume que la operación fue exitosa
            mensaje = "Operación exitosa",
            data = roles
        )
    }

    override suspend fun obtenerUsuariosAll(): APIRespuesta<List<UsuarioRespuesta>> {
        val response: HttpResponse = client.get(APIConf.USUARIOS_ENDPOINT) {
            contentType(ContentType.Application.Json)
        }
        val responseBody = response.bodyAsText()
        val jsonObject = Json.parseToJsonElement(responseBody).jsonObject
        val resultsJson = jsonObject["results"] ?: throw IllegalStateException("No se encontró el campo 'results'")
        val usuarios = Json.decodeFromJsonElement<List<UsuarioRespuesta>>(resultsJson)

        return APIRespuesta(
            estado = true, // Asume que la operación fue exitosa
            mensaje = "Operación exitosa",
            data = usuarios
        )
    }


    override suspend fun obtenerUsuario(): APIRespuesta<List<Usuario>> {
        val response: HttpResponse = client.get(APIConf.USUARIOS_ENDPOINT) {
            contentType(ContentType.Application.Json)
        }
        val responseBody = response.bodyAsText()
        // Decodifica el JSON a APIRespuesta<List<Usuario>>
        return Json.decodeFromString<APIRespuesta<List<Usuario>>>(responseBody)
    }

    override suspend fun obtenerUsuarioId(usuarioId: String): APIRespuesta<UsuarioRespuesta> {
        val response: HttpResponse = client.get("${APIConf.USUARIOS_ENDPOINT}$usuarioId") {
            contentType(ContentType.Application.Json)
        }
        val responseBody = response.bodyAsText()
        return Json.decodeFromString<APIRespuesta<UsuarioRespuesta>>(responseBody)
    }

    override suspend fun crearUsuario(usuario: Usuario): APIRespuesta<UsuarioRespuesta> {
        return try {
            val formData = formData {
                append("nombre", usuario.nombre)
                append("correo", usuario.correo)
                append("contraseña", usuario.contraseña)
                append("rol", usuario.rol)
            }

            val response: HttpResponse = client.submitFormWithBinaryData(
                url = APIConf.USUARIOS_ENDPOINT,
                formData = formData,
            )

            val apiRespuesta = response.body<APIRespuesta<UsuarioRespuesta>>()
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

    override suspend fun crearPerfil(perfil: Profile): APIRespuesta<ProfileRespuesta> {
        return try {

            val response: HttpResponse = client.post(APIConf.PERFIL_ENDPOINT){
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(perfil))
            }
            // Procesar la respuesta desde el servidor
            val apiRespuesta = response.body<APIRespuesta<ProfileRespuesta>>()
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

    override suspend fun modificarPerfil(perfil: ProfileModificar): APIRespuesta<ProfileModificar> {
        return try {
            val response: HttpResponse = client.put(APIConf.PERFIL_ENDPOINT+"${perfil.usuario}"){
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(perfil))
            }
            // Procesar la respuesta desde el servidor
            val apiRespuesta = response.body<APIRespuesta<ProfileModificar>>()
            apiRespuesta
        } catch (e: Exception) {
            Log.e("update perfil", "Error al actualizar el perfil: ${e.message}")
            // Manejo de errores
            APIRespuesta(
                estado = false,
                mensaje = "Error al crear el perfil: ${e.message}",
                data = null
            )
        }
    }

    override suspend fun obtenerPerfil(usuarioId:String): APIRespuesta<ProfileRespuesta> {
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
            Json.decodeFromString<APIRespuesta<ProfileRespuesta>>(responseBody)
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

class UsuariosService(private val apiService: UsuarioApi) : Usuarios {
    override suspend fun obtenerRol(): APIRespuesta<List<Rol>> {
        return try {
            val response: List<Rol> = apiService.obtenerRol()

            APIRespuesta(
                estado = true,
                mensaje = "Operación exitosa",
                data = response
            )
        } catch (e: Exception) {
            APIRespuesta(
                estado = false,
                mensaje = "Error: ${e.message}",
                data = emptyList()
            )
        }

    }

    override suspend fun obtenerUsuariosAll(): APIRespuesta<List<UsuarioRespuesta>> {
        return try {
            // Llamada a la API utilizando Retrofit
            val usuarios: List<UsuarioRespuesta> = apiService.obtenerUsuariosAll()

            // Crear y devolver la respuesta envuelta en APIRespuesta
            APIRespuesta(
                estado = true,
                mensaje = "Operación exitosa",
                data = usuarios
            )
        } catch (e: Exception) {
            // Manejo de errores (puedes personalizar según tu lógica)
            APIRespuesta(
                estado = false,
                mensaje = "Error: ${e.message}",
                data = emptyList() // Puedes retornar emptyList() o null si prefieres
            )
        }
    }

    override suspend fun obtenerUsuario(): APIRespuesta<List<Usuario>> {
        TODO("Not yet implemented")
    }

    override suspend fun obtenerUsuarioId(usuarioId: String): APIRespuesta<UsuarioRespuesta> {
        return try {
            val usuario: APIRespuesta<UsuarioRespuesta> = apiService.obtenerUsuarioId(usuarioId)
            usuario
        }catch (e: Exception){
         APIRespuesta(
            estado = false,
            mensaje = "Error: ${e.message}",
            data = null
        )
        }
    }

    override suspend fun crearUsuario(usuario: Usuario): APIRespuesta<UsuarioRespuesta> {
        TODO("Not yet implemented")
    }

    override suspend fun crearPerfil(perfil: Profile): APIRespuesta<ProfileRespuesta> {
        TODO("Not yet implemented")
    }

    override suspend fun modificarPerfil(perfil: ProfileModificar): APIRespuesta<ProfileModificar> {
        TODO("Not yet implemented")
    }

    override suspend fun obtenerPerfil(usuarioId: String): APIRespuesta<ProfileRespuesta> {
        return try {
            val perfil: APIRespuesta<ProfileRespuesta> = apiService.obtenerPerfil(usuarioId)
            perfil
        }catch (e: Exception){
            APIRespuesta(
                estado = false,
                mensaje = "Error: ${e.message}",
                data = null
            )
        }
    }

}