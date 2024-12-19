package com.example.ac_a.Views.Usuarios

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.Uri
import coil3.compose.AsyncImage
import coil3.toCoilUri
import com.example.ac_a.R
import com.example.ac_a.ui.theme.LightBlue80
import com.example.ac_a.utils.getFileFromUri
import com.example.ac_a.utils.toAndroidUri
import kotlinx.coroutines.launch
import org.ac.Model.Usuarios.Profile
import org.ac.Model.Usuarios.Usuario
import org.ac.Controller.Usuarios.ProfileController
import org.ac.Model.Usuarios.ProfileModificar
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
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    } else if (errorMessage != null) {
        // Si hubo un error, mostramos el mensaje de error
        Text(text = errorMessage ?: "Error desconocido", color = Color.Red, modifier = Modifier.padding(16.dp))
        SimpleAlertDialog(mensaje = errorMessage ?: "Error desconocido", title = "Error") {
            // Cuando se cierra el diálogo, puedes hacer algo si lo deseas
            // Por ejemplo, limpiar el mensaje de error:
            errorMessage = null
        }
    } else if (showForm) {
        // Mostrar el formulario de perfil cuando no se haya encontrado el perfil
        PerfilForm(usuarioId, controller, { showForm = false })
    } else if (perfilData != null) {
        // Si obtuvimos los datos correctamente, mostramos el perfil
        val usuario = perfilData!!.first
        val perfil = perfilData!!.second

        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2196F3),
                            Color(0xFF64B5F6),
                            Color(0xFFBBDEFB)
                        )
                    )
                )
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Mostrar la imagen de perfil
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .padding(16.dp)
                    .background(Color.Gray)
            ) {
                Log.d("Perfil Imagen URL", "perfil.imagen")
                AsyncImage(
                    model = "${perfil.imagen}",
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(id = R.drawable.placehorder_profile), // Agrega un placeholder si lo deseas
                    error = painterResource(id = R.drawable.error_image_profile) // Agrega una imagen de error si la carga falla
                )
            }

            // Título "Información"
            Text(
                text = "Información",
                style =  MaterialTheme.typography.titleLarge.copy(color = Color.White),
                modifier = Modifier.padding(top = 16.dp)
            )
            // Información del perfil
            Column() {
                InfoRow("Nombre", perfil.usuario)

                InfoRow("Correo", usuario.correo)

                InfoRow("Teléfono", perfil.telefono)

                InfoRow("Dirección", perfil.direccion)

                InfoRow("Carrera", perfil.carrera)

                InfoRow("Número de Control", perfil.numero_control.toString())
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón para modificar el perfil
            /*
            Button(
                onClick = { showForm = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Modificar Perfil")
            }*/
        }
    }
}


// Detalle de cada campo
@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            ),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
            ),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Composable
fun PerfilForm(usuarioId: String, controller: ProfileController, onCancel: () -> Unit) {
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var carrera by remember { mutableStateOf("") }
    var numeroControl by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var modificar by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri ->
        imagenUri = uri?.toCoilUri()
    }
    val coroutineScope = rememberCoroutineScope()

    // Llamar al controlador para obtener los datos existentes del perfil
    LaunchedEffect(usuarioId) {
        try {
            val result = controller.ObtenerPerfil(usuarioId)
            if (result != null) {
                telefono = result.second.telefono
                direccion = result.second.direccion
                carrera = result.second.carrera
                numeroControl = result.second.numero_control.toString()
                modificar = true
                // No cargamos la imagen de forma predeterminada, pero se puede agregar si es necesario.
            }
        } catch (e: Exception) {
            errorMessage = "Error al obtener el perfil: ${e.localizedMessage}"
        }
    }

    // Función para enviar los datos
    suspend fun submitForm() {
        if (telefono.isNotEmpty() && direccion.isNotEmpty() && carrera.isNotEmpty() && numeroControl.isNotEmpty() && modificar == false) {
            isLoading = true
            val numeroControlInt = numeroControl.toIntOrNull() ?: 0
            if (numeroControlInt == 0) {
                errorMessage = "El número de control debe ser un número válido."
                return
            }
            val perfil = Profile(
                telefono = telefono,
                direccion = direccion,
                carrera = carrera,
                numero_control = numeroControlInt,
                guid = "",
                usuario = usuarioId,
                imagen = ""
            )
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
        } else if (modificar == true) {
            isLoading = true
            if (!telefono.isEmpty() || !direccion.isEmpty() || !carrera.isEmpty() || !numeroControl.isEmpty()) {
                val numeroControlInt = numeroControl.toIntOrNull() ?: 0
                if (numeroControlInt == 0) {
                    errorMessage = "El número de control debe ser un número válido."
                    return
                }

                // Aquí pasamos la URI de la imagen al controlador para que el controlador se encargue de subirla
                val imagenFile = imagenUri?.let { uri ->
                    getFileFromUri(context, uri.toAndroidUri()) // Convertir la URI a un archivo
                }

                val perfilModificar = ProfileModificar(
                    telefono = telefono,
                    direccion = direccion,
                    carrera = carrera,
                    numero_control = numeroControl.toIntOrNull(),
                    imagen = null, // No necesitamos manejar la URL aquí
                    usuario = usuarioId
                )

                // Enviar la solicitud para actualizar el perfil
                controller.modificarPerfil(
                    perfilModificar,
                    imagenFile, // Enviar el archivo de imagen
                    onSuccess = { respuesta ->
                        isLoading = false
                        successMessage = "Perfil actualizado con éxito."
                    },
                    onError = { error ->
                        isLoading = false
                        errorMessage = error
                    }
                )
            }
        }
    }



    // Formulario para capturar la información del perfil
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2196F3),
                        Color(0xFF64B5F6),
                        Color(0xFFBBDEFB)
                    )
                )
            ),
    ) {
        Text(text = "Formulario de Perfil",
            style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
            modifier = Modifier.padding(top = 16.dp,start = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
        ) {
            Text(
                text = "Teléfono",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Dirección",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
            TextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Carrera",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
            TextField(
                value = carrera,
                onValueChange = { carrera = it },
                label = { Text("Carrera") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Número de Control",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
            TextField(
                value = numeroControl,
                onValueChange = { numeroControl = it },
                label = { Text("Número de Control") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Botón para seleccionar imagen
        Button(
            onClick = {
                imagePickerLauncher.launch("image/*") // Permite seleccionar una imagen
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text("Seleccionar Imagen")
        }

        if (imagenUri != null) {
            // Mostrar la imagen seleccionada en vez de la ubicación
            AsyncImage(
                model = imagenUri,
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape) // Hacer la imagen circular
                    .padding(8.dp),
                placeholder = painterResource(id = R.drawable.placehorder_profile), // Placeholder mientras se carga
                error = painterResource(id = R.drawable.error_image_profile) // Imagen de error si la carga falla
            )
        }


        // Botón para guardar el perfil
        Button(
            onClick = {
                coroutineScope.launch {
                    submitForm() // Ejecuta submitForm dentro del contexto de una corrutina
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Guardar Perfil")
        }

        Button(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text("Cancelar")
        }

        // Mostrar mensajes de error o éxito
        if (isLoading) {
            CircularProgressIndicator()
        }

        // Mostrar mensaje de éxito
        successMessage?.let {
            SimpleAlertDialog(mensaje = it, title = "Éxito") {
                // Llamar a esta función cuando el usuario presiona "Aceptar" en el diálogo de éxito
                successMessage = null
                onCancel()
            }
        }

        // Si hay un mensaje de error, mostramos el diálogo
        errorMessage?.let {
            SimpleAlertDialog(mensaje = it, title = "Error") {
                // Llamar a esta función cuando el usuario presiona "Aceptar" en el diálogo de error
                errorMessage = null
            }
        }
    }
}

@Composable
fun SimpleAlertDialog(mensaje: String,title:String, onDismiss: () -> Unit) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false }, // Cierra el diálogo al tocar fuera
            title = { Text(text = title) },
            text = { Text(mensaje) },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onDismiss() // Llamar a la función que maneja el cierre
                }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
