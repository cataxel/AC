package org.ac.Controller.Usuarios

import com.example.ac_a.APIRespuesta
import org.ac.Model.Usuarios.Profile
import org.ac.Model.Usuarios.Usuario
import org.ac.Model.Usuarios.UsuarioRespuesta
import org.ac.service.Usuarios.Usuarios
import java.io.File

class UsuariosController(private val usuariosService: Usuarios) {

    // Metodo para obtener la lista de usuarios
    suspend fun obtenerUsuarios(): APIRespuesta<List<Usuario>> {
        return try {
            usuariosService.obtenerUsuario()
        } catch (e: Exception) {
            // Puedes manejar errores aquí, y devolver una respuesta en caso de fallo
            APIRespuesta(
                estado = false,
                mensaje = "Error al obtener usuarios: ${e.message}",
                data = null
            )
        }
    }

    suspend fun obtenerUsuarioPorId(usuarioId:String): APIRespuesta<Usuario> {
        return usuariosService.obtenerUsuarioId(usuarioId)
    }

    // Metodo para crear un usuario, llamando al servicio correspondiente
    suspend fun crearUsuario(usuario: Usuario): APIRespuesta<UsuarioRespuesta> {
        return try {
            usuariosService.crearUsuario(usuario)
        } catch (e: Exception) {
            APIRespuesta(
                estado = false,
                mensaje = "Error al crear usuario: ${e.message}",
                data = null
            )
        }
    }
}

class ProfileController(private val usuariosService: Usuarios) {

    suspend fun crearPerfil(
        perfil: Profile,
        imagen: File?,
        onSuccess: (APIRespuesta<Profile>) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Aquí puedes llamar al servicio para crear el perfil, asegurándote de pasar la imagen si existe
            val respuesta = usuariosService.crearPerfil(perfil, imagen)

            if (respuesta.estado) {
                // Si la respuesta es exitosa, llama a la función de éxito
                onSuccess(respuesta)
            } else {
                // Si hay un error en la respuesta, llama a la función de error
                onError(respuesta.mensaje ?: "Error desconocido al crear el perfil")
            }
        } catch (e: Exception) {
            // Manejo de excepciones
            onError("Error al comunicarse con el servidor: ${e.message}")
        }
    }

    suspend fun ObtenerPerfil(usuarioId: String): Pair<Usuario, Profile>? {
        val perfilRespuesta = usuariosService.obtenerPerfil(usuarioId)

        // Verificar si el perfil fue encontrado
        if (perfilRespuesta.estado && perfilRespuesta.data != null) {
            val usuario = usuariosService.obtenerUsuarioId(usuarioId)

            if (usuario.estado && usuario.data != null) {
                return Pair(usuario.data, perfilRespuesta.data)
            }
        }

        // Si no se encuentra el perfil, devuelve null o maneja el caso específico
        return null
    }

}
