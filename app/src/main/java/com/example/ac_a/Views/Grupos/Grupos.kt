package com.example.ac_a.Views.Grupos

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ac_a.Controller.Grupos.GruposController
import com.example.ac_a.Model.Actividades.Actividad
import com.example.ac_a.Model.Grupos.Grupo
import com.example.ac_a.Model.Grupos.GrupoRequest
import com.example.ac_a.service.Actividades.ActividadServicio
import com.example.ac_a.service.Actividades.ActividadServicioRetrofit
import com.example.ac_a.service.Actividades.interfaces.Actividades
import com.example.ac_a.service.Grupos.GrupoServicio
import com.example.ac_a.service.Grupos.GruposServicioRetrofit
import com.example.ac_a.service.Grupos.InscripcionServicio
import com.example.ac_a.ui.theme.LightBlue80
import kotlinx.coroutines.launch
import org.ac.APIConf.NetworkClient
import org.ac.APIConf.RetrofitClient
import org.ac.Model.Usuarios.Rol
import org.ac.Model.Usuarios.UsuarioRespuesta
import org.ac.service.Usuarios.Usuarios
import org.ac.sessionManager.UserSessionManager

@Composable
fun Grupos(controller: GruposController, actividadNombre: String, navController: NavController) {
    var grupos by remember { mutableStateOf(listOf<Grupo>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    //val usuarioROL by remember { mutableStateOf( UserSessionManager(context , NetworkClient.httpClient, Usuarios(NetworkClient.httpClient)).getRolUser()) }
    val usuarioROL by remember {mutableStateOf( context.getSharedPreferences("user_session", Context.MODE_PRIVATE).getString("Rol","")) }


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
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBlue80)
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
            if (usuarioROL =="Administración"){
                Button(onClick = {
                    navController.navigate("crear_actializar_Grupo?id=${0}&guid=${""}&descripcion=${""}&ubicacion=${""}&hora_inicial=${""}&hora_final=${""}&fecha_inicial=${""}&fecha_final=${""}&capacidad=${0}&usuario_nombre=${""}&actividad_descripcion=${actividadNombre}")
                }) {
                    Text(text = "Crear Grupo")
                }
            }

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
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = grupo.descripcion,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E88E5)
                                    ),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                if(usuarioROL != "Estudiante"){
                                    IconButton(onClick = {
                                        navController.navigate("crear_actializar_Grupo?id=${grupo.id}&guid=${grupo.guid}&descripcion=${grupo.descripcion}&ubicacion=${grupo.ubicacion}&hora_inicial=${grupo.hora_inicial}&hora_final=${grupo.hora_final}&fecha_inicial=${grupo.fecha_inicial}&fecha_final=${grupo.fecha_final}&capacidad=${grupo.capacidad}&usuario_nombre=${grupo.usuario_nombre}&actividad_descripcion=${grupo.actividad_descripcion}")
                                    }){
                                        Icon(
                                            imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                                            contentDescription = "Actualizar",
                                            tint = Color.Green
                                        )
                                    }
                                }else{
                                    Text("...")
                                }
                            }

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
                            val corrutinaScope = rememberCoroutineScope()
                            //val usuarioGUID by remember { mutableStateOf( UserSessionManager(context , NetworkClient.httpClient, Usuarios(NetworkClient.httpClient)).getUserId()) }
                            val usuarioGUID by remember {mutableStateOf( context.getSharedPreferences("user_session", Context.MODE_PRIVATE).getString("user_guid","")) }
                            var isCargando by remember { mutableStateOf(false) }
                            var showDialogEliminar by remember { mutableStateOf(false) }
                            var showDialog by remember { mutableStateOf(false) }

                            suspend fun submitForm(): Boolean{
                                val inscripcionServicio = InscripcionServicio(NetworkClient.httpClient)
                                val apiRespuesta = inscripcionServicio.crearInscripcion(usuarioGUID?:"", grupo.guid)
                                isCargando = false
                                return apiRespuesta.estado
                            }
                            Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                                if(usuarioROL != "Estudiante"){
                                    IconButton(onClick = { showDialogEliminar = true }) {
                                        Icon(
                                            imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color.Red
                                        )
                                    }
                                } else{
                                    Text("...")
                                }
                                if(usuarioROL == "Estudiante"){
                                    Button(
                                        onClick = {
                                            showDialog = true

                                        }
                                    ){
                                        Text(text = "Registrarse")
                                    }
                                } else{
                                    Text("...")
                                }

                                if (showDialogEliminar) {
                                    AlertDialog(
                                        onDismissRequest = { showDialogEliminar = false }, // Cierra el diálogo al tocar fuera
                                        title = { Text(text = "Grupo") },
                                        text = {
                                            Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                                                Text("Desea eliminar el grupo?")

                                                if (isCargando) {
                                                    CircularProgressIndicator()
                                                }
                                            }

                                        },
                                        confirmButton = {
                                            TextButton(onClick = {
                                                isCargando = true
                                                corrutinaScope.launch {
                                                    if(eliminarGrupo(grupo)){
                                                        showDialogEliminar = false
                                                        Toast.makeText(context, "Grupo eliminado", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        Toast.makeText(context, "Grupo NO eliminado", Toast.LENGTH_SHORT).show()
                                                    }
                                                    isCargando = false
                                                }
                                            }) {
                                                Text("Aceptar")
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = { showDialogEliminar = false }) {
                                                Text("Cancelar")
                                            }
                                        }
                                    )
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




@Composable
fun crear_actializar_Grupo(grupoActualizar: Grupo, navController: NavController) {

    var mensaje by remember { mutableStateOf("Crear") }
    var mensaje2 by remember { mutableStateOf("creado") }
    var isLoading by remember { mutableStateOf(false) }

    var descripcion by remember { mutableStateOf(grupoActualizar.descripcion) }
    var ubicacion by remember { mutableStateOf(grupoActualizar.ubicacion) }
    var hora_inicial by remember { mutableStateOf(grupoActualizar.hora_inicial) }
    var hora_final by remember { mutableStateOf(grupoActualizar.hora_final) }
    var fecha_inicial by remember { mutableStateOf(grupoActualizar.fecha_inicial) }
    var fecha_final by remember { mutableStateOf(grupoActualizar.fecha_final) }
    var capacidad by remember { mutableStateOf(grupoActualizar.capacidad) }

    var usuario_nombre by remember { mutableStateOf(UsuarioRespuesta("", "", "", "")) }  // La opción seleccionada
    var actividad_descripcion by remember { mutableStateOf(Actividad(0, "","","")) }


    if(grupoActualizar.guid!=""){
        mensaje = "Actualizar"
        mensaje2 = "actualizado"
    }

    val corrutinaScope = rememberCoroutineScope()
    suspend fun submitForm(): Boolean{
        //val grupoServicio = GrupoServicio(NetworkClient.httpClient)
        val grupoServicio = GruposServicioRetrofit(RetrofitClient.apiServiceGrupos)
        val grupo = GrupoRequest(usuario = usuario_nombre.guid, actividad = actividad_descripcion.guid, descripcion = descripcion, ubicacion = ubicacion, hora_inicial = hora_inicial, hora_final = hora_final, fecha_inicial = fecha_inicial, fecha_final = fecha_final, capacidad = capacidad)

        if(grupoActualizar.guid==""){ //Crear
            val apiRespuesta = grupoServicio.crearGrupo(grupo)
            return apiRespuesta.estado
        }
        else{ //Actualizar
            //val apiRespuesta = grupoServicio.actualizarGrupo(grupo)
            val apiRespuesta = grupoServicio.actualizarGrupo(grupoActualizar.guid, grupo)
            return apiRespuesta.estado
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Button(onClick = {navController.popBackStack()}) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }
        Text("$mensaje Grupo", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripcion") },
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
            value = ubicacion,
            onValueChange = { ubicacion = it },
            label = { Text("Ubicacion") },
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
            value = hora_inicial,
            onValueChange = { hora_inicial = it },
            label = { Text("Hora inicial") },
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
            value = hora_final,
            onValueChange = { hora_final = it },
            label = { Text("Hora final") },
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
            value = fecha_inicial,
            onValueChange = { fecha_inicial = it },
            label = { Text("Fecha inicial") },
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
            value = fecha_final,
            onValueChange = { fecha_final = it },
            label = { Text("Fecha final") },
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
            value = capacidad.toString(),
            onValueChange = {
                // Asegurarse de que solo se conviertan valores numéricos válidos
                capacidad = it.toIntOrNull() ?: 0 // Si no es un número válido, se asigna 0
            },
            label = { Text("Capacidad") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number // Muestra el teclado numérico
            ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
        // Crear una lista de usuarios
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            val usuarios = remember { mutableStateListOf<UsuarioRespuesta>() }
            var estaListo by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                try {
                    val response = Usuarios(NetworkClient.httpClient).obtenerUsuariosAll()
                    usuarios.clear()
                    usuarios.addAll(response.data ?: emptyList())
                    estaListo = true
                    for (usuario in usuarios) {
                        if (usuario.nombre == grupoActualizar.usuario_nombre) {
                            usuario_nombre = usuario  // Actualiza la opción seleccionada
                            break
                        }
                    }
                } catch (e: Exception) {
                    Log.i("e: Exception", "error: $e")
                }
            }
            if (estaListo) {
                var expanded by remember { mutableStateOf(false) }  // Para controlar el estado del menú desplegable

                OutlinedTextField(
                    modifier = Modifier.clickable { expanded = !expanded },
                    value = usuario_nombre.nombre,
                    onValueChange = {},
                    label = { Text("Usuario") },
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = Color.Black,
                        disabledLabelColor = Color.Black,
                        disabledIndicatorColor = Color.Black,
                        disabledContainerColor = Color.White,
                    )
                )
                // Menú desplegable
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }
                ) {
                    usuarios.forEach { usuarios ->
                        if(usuarios.rol == "Docente"){
                            DropdownMenuItem(
                                text = { Text(usuarios.nombre) },
                                onClick = {
                                    usuario_nombre = usuarios  // Actualiza la opción seleccionada
                                    expanded = false  // Cierra el menú desplegable
                                }
                            )
                        }
                    }
                }
            }
        }
        // Crear una lista de actividades
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            val actividades = remember { mutableStateListOf<Actividad>() }
            var estaListo by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                try {
                    //val response = ActividadServicio(NetworkClient.httpClient).obtenerActividad()
                    val response = ActividadServicioRetrofit(RetrofitClient.apiServiceActividades).obtenerActividad()
                    actividades.clear()
                    actividades.addAll(response.results /*.data*/ ?: emptyList())
                    estaListo = true
                    for (actividad in actividades) {
                        if(actividad.nombre == grupoActualizar.actividad_descripcion){
                            actividad_descripcion = actividad
                            break
                        }
                    }
                } catch (e: Exception) {
                    Log.i("e: Exception", "error: $e")
                }
            }
            if (estaListo) {
                var expanded by remember { mutableStateOf(false) }  // Para controlar el estado del menú desplegable

                OutlinedTextField(
                    modifier = Modifier.clickable { expanded = !expanded },
                    value = actividad_descripcion.nombre,
                    onValueChange = {},
                    label = { Text("Actividad") },
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = Color.Black,
                        disabledLabelColor = Color.Black,
                        disabledIndicatorColor = Color.Black,
                        disabledContainerColor = Color.White,
                    )
                )
                // Menú desplegable
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }
                ) {
                    actividades.forEach { actividad ->
                        DropdownMenuItem(
                            text = { Text(actividad.nombre) },
                            onClick = {
                                actividad_descripcion = actividad  // Actualiza la opción seleccionada
                                expanded = false  // Cierra el menú desplegable
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        val context = LocalContext.current
        Button(onClick = {
            isLoading = true
            corrutinaScope.launch {
                if(submitForm()){
                    Toast.makeText(context, "Grupo $mensaje2", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, "Grupo NO $mensaje2", Toast.LENGTH_SHORT).show()
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

suspend fun eliminarGrupo(grupo: Grupo):Boolean {
    //val grupoServicio = GrupoServicio(NetworkClient.httpClient)
    val grupoServicio = GruposServicioRetrofit(RetrofitClient.apiServiceGrupos)
    //val apiRespuesta = grupoServicio.eliminarGrupo(grupo)
    val apiRespuesta = grupoServicio.eliminarGrupo(grupo.guid)
    Log.i("Beto", "apiRespuesta: $apiRespuesta")
    Log.i("Beto", "apiRespuesta: ${apiRespuesta.estado}")
    return apiRespuesta.estado
}