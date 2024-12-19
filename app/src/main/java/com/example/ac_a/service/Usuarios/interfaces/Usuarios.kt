package org.ac.service.Usuarios.interfaces

import com.example.ac_a.APIRespuesta
import org.ac.APIConf.APIConf
import org.ac.Model.Usuarios.Profile
import org.ac.Model.Usuarios.ProfileModificar
import org.ac.Model.Usuarios.ProfileRespuesta
import org.ac.Model.Usuarios.Rol
import org.ac.Model.Usuarios.Usuario
import org.ac.Model.Usuarios.UsuarioRespuesta
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.File

interface Usuarios {
    suspend fun obtenerRol(): APIRespuesta<List<Rol>>
    suspend fun obtenerUsuariosAll(): APIRespuesta<List<UsuarioRespuesta>>
    suspend fun obtenerUsuario(): APIRespuesta<List<Usuario>>
    suspend fun obtenerUsuarioId(usuarioId:String): APIRespuesta<UsuarioRespuesta>
    suspend fun crearUsuario(usuario: Usuario): APIRespuesta<UsuarioRespuesta>
    suspend fun crearPerfil(perfil: Profile): APIRespuesta<ProfileRespuesta>
    suspend fun modificarPerfil(perfil: ProfileModificar):APIRespuesta<ProfileModificar>
    suspend fun obtenerPerfil(usuarioId: String):APIRespuesta<ProfileRespuesta>
}

interface UsuarioApi {
    @GET(APIConf.ROLES_ENDPOINT)
    suspend fun obtenerRol(): List<Rol>
    @GET(APIConf.USUARIOS_ENDPOINT)
    suspend fun obtenerUsuariosAll(): List<UsuarioRespuesta>
    @GET("${APIConf.USUARIOS_ENDPOINT}{usuarioId}")
    suspend fun obtenerUsuarioId(@Path("usuarioId") usuarioId:String): APIRespuesta<UsuarioRespuesta>
    @GET("${APIConf.PERFIL_ENDPOINT}{usuarioId}")
    suspend fun obtenerPerfil(@Path("usuarioId") usuarioId: String): APIRespuesta<ProfileRespuesta>
}