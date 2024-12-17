package com.example.ac_a.Views.Usuarios

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.Uri
import coil3.compose.AsyncImage
import coil3.toCoilUri
import com.example.ac_a.utils.getFileFromUri
import com.example.ac_a.utils.toAndroidUri
import kotlinx.coroutines.launch
import org.ac.Model.Usuarios.Profile
import org.ac.Model.Usuarios.Usuario
import org.ac.Controller.Usuarios.ProfileController
import org.ac.Model.Usuarios.ProfileRespuesta
import org.ac.Model.Usuarios.UsuarioRespuesta
import java.io.File

@Composable
fun Profile(controller: ProfileController, usuarioId: String) {
    var perfilData by remember { mutableStateOf<Pair<UsuarioRespuesta, ProfileRespuesta>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showForm by remember { mutableStateOf(false) }

    // Llamar al controlador para obtener los datos
    LaunchedEffect(usuarioId) {
        try {
            val result = controller.ObtenerPerfil(usuarioId)
            perfilData = result
            if (result == null) {
                showForm = true // Mostrar el formulario si no se encuentra el perfil
            }
        } catch (e: Exception) {
            errorMessage = "Error al obtener el perfil: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    // Si estamos cargando, mostramos un indicador de progreso
    if (isLoading) {
        CircularProgressIndicator()
    } else if (errorMessage != null) {
        // Si hubo un error, mostramos el mensaje de error
        Text(text = errorMessage ?: "Error desconocido")
    } else if (showForm) {
        // Mostrar el formulario de perfil cuando no se haya encontrado el perfil
        PerfilForm(usuarioId, controller)
    } else if (perfilData != null) {
        // Si obtuvimos los datos correctamente, mostramos el perfil
        val usuario = perfilData!!.first
        val perfil = perfilData!!.second

        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    Modifier.size(200.dp)
                ) {
                    AsyncImage(
                        model = perfil.imagen,
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(text = "Nombre: ${usuario.nombre}")
                Text(text = "Correo: ${usuario.correo}")
                Text(text = "Teléfono: ${perfil.telefono}")
                Text(text = "Dirección: ${perfil.direccion}")
                Text(text = "Carrera: ${perfil.carrera}")
                Text(text = "Número de Control: ${perfil.numero_control}")
            }
        }
    }
}

@Composable
fun PerfilForm(usuarioId: String, controller: ProfileController) {
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var carrera by remember { mutableStateOf("") }
    var numeroControl by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri ->
        imagenUri = uri?.toCoilUri()
    }
    val coroutineScope = rememberCoroutineScope()

    // Función para enviar los datos
    suspend fun submitForm() {
        if (telefono.isNotEmpty() && direccion.isNotEmpty() && carrera.isNotEmpty() && numeroControl.isNotEmpty()) {
            isLoading = true
            val numeroControlInt = numeroControl.toIntOrNull() ?: 0
            if (numeroControlInt == 0) {
                errorMessage = "El número de control debe ser un número válido."
                return
            }
            val perfil = Profile(telefono = telefono, direccion =  direccion, carrera =  carrera, numero_control = numeroControlInt, id = "", usuario = usuarioId, imagen = "")
            val imagenFile = imagenUri?.let { uri ->
                getFileFromUri(context, uri.toAndroidUri())
            }

            controller.crearPerfil(
                perfil,
                imagenFile,
                onSuccess = { respuesta ->
                    isLoading = false
                    successMessage = "Perfil creado con éxito."
                },
                onError = { error ->
                    isLoading = false
                    errorMessage = error
                }
            )
        } else {
            errorMessage = "Todos los campos deben ser llenados."
        }
    }

    // Formulario para capturar la información del perfil
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Formulario de Perfil")

        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = carrera,
            onValueChange = { carrera = it },
            label = { Text("Carrera") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = numeroControl,
            onValueChange = { numeroControl = it },
            label = { Text("Número de Control") },
            modifier = Modifier.fillMaxWidth()
        )

        // Botón para seleccionar imagen
        Button(
            onClick = {
                imagePickerLauncher.launch("image/*") // Permite seleccionar una imagen
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Seleccionar Imagen")
        }

        if (imagenUri != null) {
            Text("Imagen seleccionada: ${imagenUri.toString()}")
        }

        // Botón para guardar el perfil
        Button(
            onClick = {
                coroutineScope.launch {
                    submitForm() // Ejecuta submitForm dentro del contexto de una corrutina
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Perfil")
        }

        // Mostrar mensajes de error o éxito
        if (isLoading) {
            CircularProgressIndicator()
        }

        errorMessage?.let {
            Text(text = it, color = Color.Red)
        }

        successMessage?.let {
            Text(text = it, color = Color.Green)
        }
    }
}
