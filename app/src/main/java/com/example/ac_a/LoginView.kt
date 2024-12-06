package com.example.ac_a

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import org.ac.service.Usuarios.Usuario


class LoginActivity : ComponentActivity() {

    lateinit var usuarioService: Usuario
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent {
            usuarioService = Usuario(NetworkClient.httpClient)
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "Login") {
                composable("Login") {

                    Login(navController = navController,usuarioService)
                }
                //composable("Registrar") { Register(navController = navController) }
            }
        }
    }
}

@Composable
fun Login(navController: NavController, usuarioServicio:Usuario){
    val imagenes = remember { CloudinaryImages() }
    val photoUrls = remember { mutableStateListOf<String>() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val photos = ImagenesControlador(imagenes).listarImagenes().data
        photos?.let {
            photoUrls.addAll(it.map {loginImage -> loginImage.url})
        }
        isLoading = false
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
fun LoginOverlay(navController: NavController, usuarioServicio:Usuario) {
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
            Button(onClick = { navController.navigate("register") }) {
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