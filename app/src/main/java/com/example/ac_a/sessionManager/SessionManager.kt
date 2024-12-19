package org.ac.sessionManager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.example.ac_a.APIRespuesta
import com.example.ac_a.LoginActivity
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.ac.APIConf.APIConf
import org.ac.Model.Usuarios.TokenSession
import org.ac.service.Usuarios.Usuarios
import org.ac.service.Usuarios.UsuariosService
import org.ac.sessionManager.interfaces.SessionManager


class UserSessionManager(
    private val context: Context,
    private val client:HttpClient,
    private val usuarioServicio: UsuariosService /*Usuarios*/
):SessionManager {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_session",Context.MODE_PRIVATE)
    override fun isUserLogginIn(): Boolean {
        return sharedPreferences.contains("accessToken")
    }

    override fun getUserId(): String {
        return sharedPreferences.getString("user_guid","")?:""
    }

    override fun getRolUser(): String {
        return sharedPreferences.getString("Rol","")?:""
    }

    override fun getUserToken(): String {
        return sharedPreferences.getString("accessToken","")?:""
    }

    override suspend fun getUserRol(): String {
        return sharedPreferences.getString("Rol","")?:""
    }

    override suspend fun login(email: String, password: String) {
        try {
            val response: HttpResponse = client.post(APIConf.LOGIN_ENDPOINT){
                contentType(ContentType.Application.Json)
                setBody(mapOf("correo" to email, "contraseña" to password))
            }
            val responseText = response.bodyAsText()
            val apiRespuesta = Json.decodeFromString<APIRespuesta<TokenSession>>(responseText)

            if (apiRespuesta.estado && apiRespuesta.data != null) {
                //usuarioState.value = controller.obtenerUsuarioPorId(usuarioId)
                saveSession(apiRespuesta.data)
                val userGuid = apiRespuesta.data.user_guid
                val usuarioResponse = usuarioServicio.obtenerUsuarioId(userGuid)

                if (usuarioResponse.estado && usuarioResponse.data != null) {
                    saveUserRole(usuarioResponse.data.rol)
                }else{
                    Log.e("Role","Error: ${usuarioResponse.mensaje}")
                    throw Exception("Error al obtener la informacion de usuario")
                }

            }else{
                Log.e("Login","Error: ${apiRespuesta.mensaje}")
                throw Exception(apiRespuesta.mensaje)
            }
        }catch (e: Exception) {
            Log.e("Login","Error: ${e.message}")
            throw Exception("Error al iniciar session")
        }
    }

    override fun logout() {
        clearSession()
        val intent = Intent(context,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    private fun saveSession(tokenSession: TokenSession) {
        with(sharedPreferences.edit()) {
            putString("user_guid", tokenSession.user_guid)
            putString("refreshToken", tokenSession.refresh)
            putString("accessToken", tokenSession.access)
            apply()
        }
    }

    private fun saveUserRole(role:String){
        with(sharedPreferences.edit()){
            putString("Rol",role)
            apply()
        }
    }

    private fun clearSession() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

}