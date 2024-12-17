package com.example.ac_a.Views.Usuarios

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.ac_a.APIRespuesta
import org.ac.Controller.Usuarios.UsuariosController
import org.ac.Model.Usuarios.Usuario
import org.ac.Model.Usuarios.UsuarioRespuesta

@Composable
fun UsuariosScreen(controller: UsuariosController,usuarioId:String) {
    val usuarioState = remember { mutableStateOf<APIRespuesta<UsuarioRespuesta>?>(null) }

    LaunchedEffect(usuarioId) {
        // Llama al controlador para obtener el usuario específico por su ID
        usuarioState.value = controller.obtenerUsuarioPorId(usuarioId)
    }

    when (val usuario = usuarioState.value) {
        null -> Text("Cargando...")
        else -> {
            if (usuario.estado) {
                Text("Nombre: ${usuario.data?.nombre}")
                Text("Correo: ${usuario.data?.correo}")
            } else {
                Text("Error: ${usuario.mensaje}")
            }
        }
    }
}
