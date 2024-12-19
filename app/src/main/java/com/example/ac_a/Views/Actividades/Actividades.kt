package com.example.ac_a.Views.Actividades

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ac_a.Controller.Actividades.ActividadesController
import com.example.ac_a.Model.Actividades.Actividad
import androidx.navigation.NavController
import com.example.ac_a.Navegation
import com.example.ac_a.service.Actividades.ActividadServicio
import com.example.ac_a.service.Actividades.ActividadServicioRetrofit
import kotlinx.coroutines.launch
import org.ac.APIConf.NetworkClient
import org.ac.APIConf.RetrofitClient
import org.ac.service.Usuarios.Usuarios
import org.ac.sessionManager.UserSessionManager

@Composable
fun Actividad(controller: ActividadesController, usuarioId: String,navController: NavController) {
    var actividades by remember { mutableStateOf(listOf<Actividad>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    //val usuarioROL by remember { mutableStateOf( UserSessionManager(context , NetworkClient.httpClient, Usuarios(NetworkClient.httpClient)).getRolUser()) }
    val usuarioROL by remember {mutableStateOf( context.getSharedPreferences("user_session", Context.MODE_PRIVATE).getString("Rol","")) }


    LaunchedEffect(Unit) {
        try {
            val response = controller.obtenerActividades()
            actividades = response.data ?: listOf()
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
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
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
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
                )
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Actividades",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (usuarioROL != "Estudiante"){
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    if (usuarioROL =="Administración"){
                        Button(onClick = {
                            navController.navigate("crear_actializar_Actividad?guidActividad=${""}&nombre=${""}&descripcion=${""}")

                        }){
                            Text(text = "Crear Actividad")
                        }
                    }

                    Button(onClick = {
                        navController.navigate("crear_actializar_Grupo?id=${0}&guid=${""}&descripcion=${""}&ubicacion=${""}&hora_inicial=${""}&hora_final=${""}&fecha_inicial=${""}&fecha_final=${""}&capacidad=${0}&usuario_nombre=${""}&actividad_descripcion=${""}")
                    }) {
                        Text(text = "Crear Grupo")
                    }
                }
            }

            Text(
                text = "Número de actividades: ${actividades.size}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            actividades.forEach { actividad ->
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
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = actividad.nombre,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E88E5)
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            if(usuarioROL == "Administración"){
                                IconButton(onClick = {
                                    navController.navigate("crear_actializar_Actividad?guidActividad=${actividad.guid}&nombre=${actividad.nombre}&descripcion=${actividad.descripcion}")
                                }){
                                    Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                                        contentDescription = "Actualizar",
                                        tint = Color.Green
                                    )
                                }
                            } else{
                                Text("...")
                            }

                        }

                        Text(
                            text = actividad.descripcion,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 18.sp,
                                color = Color(0xFF1E88E5)
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        val corrutinaScope = rememberCoroutineScope()
                        var isLoadingDelete by remember { mutableStateOf(false) }
                        var showDialog by remember { mutableStateOf(false) }

                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text(text = "Eliminar actividad") },
                                text = {
                                    Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                                        Text("Desea eliminar la actividad?")

                                        if (isLoadingDelete) {
                                            CircularProgressIndicator()
                                        }
                                    }

                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        isLoadingDelete = true
                                        corrutinaScope.launch {
                                            if(eliminarActividad(actividad))
                                                Toast.makeText(context, "Actividad eliminada", Toast.LENGTH_SHORT).show()
                                            navController.navigate(Navegation.Actividades.name)
                                            isLoadingDelete = false
                                            showDialog = false
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

                        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                            if (usuarioROL =="Administración"){
                                IconButton(onClick = {
                                    showDialog = true
                                }){
                                    Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = Color.Red
                                    )
                                }
                            } else{
                                Text("...")
                            }

                            Button(
                                onClick = {
                                    navController.navigate("grupos/${actividad.nombre}")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                                shape = MaterialTheme.shapes.medium,
                            ) {
                                Text(
                                    text = "Ver grupos",
                                )
                            }
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let {
                Text(text = it, color = Color.Red)
            }
        }
    }
}


@Composable
fun crear_actializar_Actividad(navController: NavController, guidActividad: String, nombreA: String, descripcionA: String) {

    var mensaje by remember { mutableStateOf("Crear") }
    var mensaje2 by remember { mutableStateOf("creada") }
    var nombre by remember { mutableStateOf(nombreA) }
    var descripcion by remember { mutableStateOf(descripcionA) }
    var isLoading by remember { mutableStateOf(false) }
    if(guidActividad!=""){
        mensaje = "Actualizar"
        mensaje2 = "actualizada"
    }

    val corrutinaScope = rememberCoroutineScope()
    suspend fun submitForm(): Boolean{
        //val inscripcionServicio = ActividadServicio(NetworkClient.httpClient)
        val apiService = RetrofitClient.apiServiceActividades
        val inscripcionServicio = ActividadServicioRetrofit(apiService)
        val actividad = Actividad(id=0, guid=guidActividad, nombre, descripcion)
        isLoading = false
        if(guidActividad==""){ //Crear
            val apiRespuesta = inscripcionServicio.crearActividad(actividad)
            return apiRespuesta.estado
        }
        else{ //Actualizar
            val apiRespuesta = inscripcionServicio.actualizarActividad(actividad.guid,actividad)
            //val apiRespuesta = inscripcionServicio.actualizarActividad(actividad)
            return apiRespuesta.estado
        }
    }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("$mensaje Actividad", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        val context = LocalContext.current
        Button(onClick = {
            isLoading = true
            corrutinaScope.launch {
                if(submitForm()){
                    Toast.makeText(context, "Actividad $mensaje2", Toast.LENGTH_SHORT).show()
                    navController.navigate(Navegation.Actividades.name)
                } else {
                    Toast.makeText(context, "Actividad NO $mensaje2", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }
        }){
            Text(text = mensaje)
        }
        if (isLoading) {
            CircularProgressIndicator()
        }
    }

}

suspend fun eliminarActividad(actividad: Actividad):Boolean {
    //val actividadServicio = ActividadServicio(NetworkClient.httpClient)
    val apiService = RetrofitClient.apiServiceActividades
    val actividadServicio = ActividadServicioRetrofit(apiService)
    val apiRespuesta = actividadServicio.eliminarActividad(actividad.guid)
    //val apiRespuesta = actividadServicio.eliminarActividad(actividad)
    return apiRespuesta.estado
}