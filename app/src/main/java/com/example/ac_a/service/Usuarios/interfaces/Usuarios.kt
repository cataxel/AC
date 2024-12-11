package org.ac.service.Usuarios.interfaces

import com.example.ac_a.APIRespuesta
import org.ac.Model.Usuarios.Profile
import org.ac.Model.Usuarios.Rol
import org.ac.Model.Usuarios.Usuario
import java.io.File

interface Usuarios {
    suspend fun obtenerUsuario(): APIRespuesta<List<Usuario>>
    suspend fun obtenerUsuarioId(usuarioId:String): APIRespuesta<Usuario>
    suspend fun crearUsuario(usuario: Usuario): APIRespuesta<Usuario>
    suspend fun crearPerfil(perfil: Profile, imagen: File?=null): APIRespuesta<Profile>
    suspend fun obtenerPerfil(usuarioId: String):APIRespuesta<Profile>
}