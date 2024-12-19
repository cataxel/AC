package com.example.ac_a

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import com.example.ac_a.Controller.loginLogout.ImagenesControlador
import com.example.ac_a.service.Cloudinary.CloudinaryImages
import org.ac.sessionManager.UserSessionManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ac_a.Controller.loginLogout.Controlador
import kotlinx.coroutines.launch
import org.ac.APIConf.NetworkClient
import org.ac.APIConf.RetrofitClient
import org.ac.Model.Usuarios.Rol
import org.ac.Model.Usuarios.Usuario
import org.ac.service.Usuarios.UsuariosService


class LoginActivity : ComponentActivity() {

    //lateinit var usuarioService: Usuarios
    lateinit var usuarioService: UsuariosService

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent {
            val usuarioApi = RetrofitClient.apiServiceUsuarios
            //usuarioService = Usuarios(NetworkClient.httpClient)
            usuarioService = UsuariosService(usuarioApi)
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "Login") {
                composable("Login") {

                    Login(navController = navController,usuarioService)
                }
                composable("Register") {
                    Register(navController = navController, usuarioService)
                }
            }
        }
    }
}

@Composable
fun Login(navController: NavController, usuarioServicio:UsuariosService /*Usuarios*/){
    val imagenes = remember { CloudinaryImages() }
    val photoUrls = remember { mutableStateListOf<String>() }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val photos = ImagenesControlador(imagenes).listarImagenes().data
            photos?.let {
                photoUrls.addAll(it.map { loginImage -> loginImage.url })
            }
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }
    if (isLoading){
        CircularProgressIndicator()
    }else{
        Box(modifier = Modifier.fillMaxSize()) {
            PhotoCarousel(photoUrls = photoUrls)
            LoginOverlay(navController, usuarioServicio)
        }
    }
}

@Composable
fun LoginOverlay(navController: NavController, usuarioServicio: UsuariosService /*Usuarios*/) {
    val context = LocalContext.current
    val sessionManager = UserSessionManager(context, NetworkClient.httpClient, usuarioServicio)
    val coroutineScope = rememberCoroutineScope()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.9f))
                .padding(32.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Image(painter = painterResource(R.drawable.ac_icon), contentDescription = "Halcones Ac")
            Text(text = "Login", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                coroutineScope.launch {
                    try {
                        Controlador(sessionManager).Login(email,password)
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        (context as? ComponentActivity)?.finish()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Iniciar Sesión")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate("Register") }) {
                Text("Registrar")
            }
        }
    }
}

@Composable
fun PhotoCarousel(photoUrls: List<String>){
    var currentImageIndex by remember { mutableIntStateOf(0) }
    var coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        AsyncImage(
            model = photoUrls[currentImageIndex],
            ContentScale.Fit.toString(),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        )
        LaunchedEffect(key1 = currentImageIndex) {
            delay(5000) // Delay for 5 seconds
            currentImageIndex = (currentImageIndex + 1) % photoUrls.size
        }
    }
}





@Composable
fun Register(navController: NavController, usuarioServicio:UsuariosService /*Usuarios*/) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val roles = remember { mutableStateListOf<Rol>() }
    var isLoading by remember { mutableStateOf(false) }
    var isLoading2 by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            //val response = usuarioServicio.obtenerRol()
            val response = usuarioServicio.obtenerRol()
            roles.clear()
            roles.addAll(response.data ?: emptyList())
            isLoading = true
        } catch (e: Exception) {
            errorMessage = "Error al obtener roles: ${e.message}"
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.9f))
                .padding(32.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            IconButton(onClick = { navController.navigate("login") }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }


            Spacer(modifier = Modifier.height(16.dp))
            var nombre by remember { mutableStateOf("") }
            var correo by remember { mutableStateOf("") }
            var contraseña by remember { mutableStateOf("") }

            var rolValue by remember { mutableStateOf(false) }
            var rolMensaje by remember { mutableStateOf("") }




            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") },
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = contraseña,
                onValueChange = { contraseña = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = rolMensaje,
                onValueChange = { rolMensaje = it },
                label = { Text("Mensaje de Rol") },
            )
            Button(onClick = {
                if(rolValue){rolValue=false}
                else if(rolMensaje=="Rol"){ rolValue = true }
            }
            ){
                if (rolValue){
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                } else{
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                }
            }
            if (isLoading) {
                var expanded by remember { mutableStateOf(false) }  // Para controlar el estado del menú desplegable
                var selectedOption by remember { mutableStateOf(roles[2]) }  // La opción seleccionada

                val corrutinaScope = rememberCoroutineScope()

                suspend fun submitForm():Boolean{
                    val usuario = Usuario(guid="", nombre, correo, contraseña, rol=selectedOption.guid)
                    val apiRespuesta = usuarioServicio.crearUsuario(usuario)
                    return apiRespuesta.estado
                }

                if(rolValue){

                    Column {
                        OutlinedTextField(
                            modifier = Modifier.clickable { expanded = !expanded },
                            value = selectedOption.nombre,
                            onValueChange = {},
                            label = { Text("Rol") },
                            enabled = false,
                            colors = TextFieldDefaults.colors(
                                disabledTextColor = Color.Black,
                                disabledLabelColor = Color.Black,
                                disabledIndicatorColor = Color.Black,
                                disabledContainerColor = Color.White,
                            )
                        )
                        // Menú desplegable
                        DropdownMenu( expanded = expanded,  onDismissRequest = { expanded = false }
                        ) {
                            roles.forEach { roles ->
                                DropdownMenuItem(
                                    text = { Text(roles.nombre) },
                                    onClick = {
                                        selectedOption = roles  // Actualiza la opción seleccionada
                                        expanded = false  // Cierra el menú desplegable
                                    }
                                )
                            }
                        }
                    }

                }


                Spacer(modifier = Modifier.height(16.dp))
                val context = LocalContext.current
                val sessionManager = UserSessionManager(context, NetworkClient.httpClient, usuarioServicio)
                var mensaje by remember { mutableStateOf("") }
                Button(onClick = {
                    isLoading2 = true
                    corrutinaScope.launch {
                        if(submitForm()){
                            try {
                                Controlador(sessionManager).Login(correo,contraseña)
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                (context as? ComponentActivity)?.finish()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }

                        } else{
                            isLoading2 = false
                            mensaje = "No se puede craer el Usuario"
                            if (nombre == "")
                                mensaje += "\nSe requiere Nombre"
                            if (correo == "")
                                mensaje += "\nSe requiere Correo"
                            if (contraseña == "")
                                mensaje += "\nSe requiere Contraseña"
                        }
                    }

                }) {
                    Text("Registrar")
                }
                if (isLoading2){
                    CircularProgressIndicator()

                }
                if (mensaje!="")
                    if(SimpleAlertDialog(mensaje))
                        mensaje=""

            }
        }
    }
}


@Composable
fun SimpleAlertDialog(mensaje:String):Boolean {
    var showDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { showDialog = true }, // Cierra el diálogo al tocar fuera
        title = { Text(text = "Error al Crear Usuario") },
        text = { Text(mensaje) },
        confirmButton = {
            TextButton(onClick = { showDialog = true }) {
                Text("Aceptar")
            }
        }
    )

    return showDialog
}
