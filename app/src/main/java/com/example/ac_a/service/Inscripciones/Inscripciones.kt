package com.example.ac_a.service.Grupos

import com.example.ac_a.APIRespuesta
import com.example.ac_a.Model.Grupos.Grupo
import com.example.ac_a.Model.Grupos.GrupoResponse
import com.example.ac_a.Model.Inscripciones.Inscripcion
import com.example.ac_a.service.Grupos.interfaces.Inscripciones
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.ac.APIConf.APIConf
import org.ac.Model.Usuarios.Usuario
import org.ac.Model.Usuarios.UsuarioRespuesta

class InscripcionServicio(private val client: HttpClient) : Inscripciones {
    override suspend fun crearInscripcion(usuario: String, grupo: String): APIRespuesta<Inscripcion> {
        return try {
            val formData = formData {
                append("estado", "inscrito")
                append("usuario", usuario)
                append("grupo", grupo)
            }

            val response: HttpResponse = client.submitFormWithBinaryData(
                url = APIConf.INSCRIPCIONES_ENDPOINT,
                formData = formData,
            )

            val apiRespuesta = response.body<APIRespuesta<Inscripcion>>()
            apiRespuesta

        }catch (e: Exception){
            // Manejo de errores
            APIRespuesta(
                estado = false,
                mensaje = "Error al crear la inscripcion: ${e.message}",
                data = null
            )
        }
    }
}
