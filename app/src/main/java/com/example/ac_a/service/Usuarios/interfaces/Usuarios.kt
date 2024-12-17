package org.ac.service.Usuarios.interfaces

import com.example.ac_a.APIRespuesta
import org.ac.Model.Usuarios.Profile
import org.ac.Model.Usuarios.ProfileRespuesta
import org.ac.Model.Usuarios.Rol
import org.ac.Model.Usuarios.Usuario
import org.ac.Model.Usuarios.UsuarioRespuesta
import java.io.File

interface Usuarios {
    suspend fun obtenerRol(): APIRespuesta<List<Rol>>
    suspend fun obtenerUsuario(): APIRespuesta<List<Usuario>>
    suspend fun obtenerUsuarioId(usuarioId:String): APIRespuesta<UsuarioRespuesta>
    suspend fun crearUsuario(usuario: Usuario): APIRespuesta<UsuarioRespuesta>
    suspend fun crearPerfil(perfil: Profile): APIRespuesta<Profile>
    suspend fun obtenerPerfil(usuarioId: String):APIRespuesta<ProfileRespuesta>
}