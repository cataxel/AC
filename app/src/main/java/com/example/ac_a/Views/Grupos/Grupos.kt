package com.example.ac_a.Views.Grupos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ac_a.Controller.Grupos.GruposController
import com.example.ac_a.Model.Grupos.Grupo

@Composable
fun Grupos(controller: GruposController, actividadId: Int) {
    var grupos by remember { mutableStateOf(listOf<Grupo>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(actividadId) {
        try {
            val response = controller.obtenerGrupos(actividadId)
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
                            text = grupo.descripcion,
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
                    }
                }
            }

            errorMessage?.let {
                Text(text = it, color = Color.Red)
            }
        }
    }
}
