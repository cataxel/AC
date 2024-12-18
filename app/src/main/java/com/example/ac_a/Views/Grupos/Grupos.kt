package com.example.ac_a.Views.Grupos

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ac_a.Controller.Grupos.GruposController
import com.example.ac_a.Model.Grupos.Grupo
import com.example.ac_a.Model.Inscripciones.Inscripcion
import com.example.ac_a.service.Grupos.InscripcionServicio
import kotlinx.coroutines.launch
import org.ac.APIConf.NetworkClient
import org.ac.Model.Usuarios.Usuario
import org.ac.service.Usuarios.Usuarios
import org.ac.sessionManager.UserSessionManager

@Composable
fun Grupos(controller: GruposController, actividadNombre: String) {
    var grupos by remember { mutableStateOf(listOf<Grupo>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = controller.obtenerGrupos()
            grupos = response.data ?: listOf()
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Grupos Disponibles",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            grupos.forEach { grupo ->
                if(grupo.actividad_descripcion == actividadNombre)
                {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = grupo.descripcion,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E88E5)
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Docente: " + grupo.usuario_nombre,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 18.sp,
                                    color = Color(0xFF1E88E5)
                                ),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Text(
                                text = "Ubicación: ${grupo.ubicacion}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 18.sp,
                                    color = Color(0xFF1E88E5)
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Horario: ${grupo.hora_inicial} - ${grupo.hora_final}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 18.sp,
                                    color = Color(0xFF1E88E5)
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Fechas: ${grupo.fecha_inicial} - ${grupo.fecha_final}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 18.sp,
                                    color = Color(0xFF1E88E5)
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Capacidad: ${grupo.capacidad}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 18.sp,
                                    color = Color(0xFF1E88E5)
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            val context = LocalContext.current
                            val corrutinaScope = rememberCoroutineScope()
                            val usuarioGUID by remember { mutableStateOf( UserSessionManager(context , NetworkClient.httpClient, Usuarios(NetworkClient.httpClient)).getUserId()) }
                            var isCargando by remember { mutableStateOf(false) }
                            var showDialog by remember { mutableStateOf(false) }

                            suspend fun submitForm(): Boolean{
                                val inscripcionServicio = InscripcionServicio(NetworkClient.httpClient)
                                val apiRespuesta = inscripcionServicio.crearInscripcion(usuarioGUID, grupo.guid)
                                isCargando = false
                                return apiRespuesta.estado
                            }
                            Row {
                                Button(
                                    onClick = {
                                        showDialog = true

                                    }
                                ){
                                    Text(text = "Registrarse")
                                }


                                if (showDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showDialog = false }, // Cierra el diálogo al tocar fuera
                                        title = { Text(text = "Inscripcion") },
                                        text = {
                                            Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                                                Text("Desea registrarse en el grupo?")

                                                if (isCargando) {
                                                    CircularProgressIndicator()
                                                }
                                            }

                                        },
                                        confirmButton = {
                                            TextButton(onClick = {
                                                isCargando = true
                                                corrutinaScope.launch {
                                                    if(submitForm()){
                                                        showDialog = false
                                                        Toast.makeText(context, "Inscripcion exitosa", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        Toast.makeText(context, "Inscripcion NO realizada", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }) {
                                                Text("Aceptar")
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = { showDialog = false }) {
                                                Text("Cancelar")
                                            }
                                        }
                                    )
                                }
                            }

                        }
                    }
                }
            }

            errorMessage?.let {
                Text(text = it, color = Color.Red)
            }
        }
    }
}
