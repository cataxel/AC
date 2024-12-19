package com.example.ac_a

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.unit.dp
import com.example.ac_a.ui.theme.ACaTheme
import org.ac.APIConf.NetworkClient
import org.ac.APIConf.RetrofitClient
import org.ac.service.Usuarios.Usuarios
import org.ac.service.Usuarios.UsuariosService
import org.ac.sessionManager.UserSessionManager
import org.ac.sessionManager.interfaces.SessionManager

class MainActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager
    //private lateinit var usuarioService: Usuarios
    private lateinit var usuarioService: UsuariosService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val usuarioApi = RetrofitClient.apiService
        //usuarioService = Usuarios(NetworkClient.httpClient)
        usuarioService = UsuariosService(usuarioApi)
        sessionManager = UserSessionManager(this, NetworkClient.httpClient,usuarioService)

        enableEdgeToEdge()
        setContent {
            ACaTheme {
                var currentScreen by remember { mutableStateOf(determineInitialScreen()) }

                when (currentScreen) {
                    MainActivity.Screen.ConnectionChecker -> ConnectionChecker { isConnected ->
                        if (isConnected) currentScreen = determineInitialScreen()
                    }
                    MainActivity.Screen.Login -> {
                        redirectToLogin()
                        // Este bloque nunca regresa al flujo porque redirectToLogin finaliza la actividad.
                    }
                    MainActivity.Screen.App -> App(sessionManager)
                }
            }
        }
    }

    private fun determineInitialScreen(): Screen {
        return if (!isConnectedToWifi()) {
            MainActivity.Screen.ConnectionChecker
        } else if (!sessionManager.isUserLogginIn()) {
            MainActivity.Screen.Login
        } else {
            MainActivity.Screen.App
        }
    }

    private fun redirectToLogin() {
        // Redirige al usuario a la pantalla de inicio de sesión
        val intent = Intent(this, LoginActivity()::class.java)
        startActivity(intent)
        finish() // Finaliza la actividad actual para evitar volver con el botón "Atrás"
    }

    private fun isConnectedToWifi(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    @Composable
    fun ConnectionChecker(onConnectionRestored: (Boolean) -> Unit) {
        var isConnected by remember { mutableStateOf(isConnectedToWifi()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isConnected) "Estás conectado a Wi-Fi" else "No estás conectado a Wi-Fi",
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = {
                    isConnected = isConnectedToWifi()
                    if (isConnected) {
                        Toast.makeText(this@MainActivity, "Conexión restablecida", Toast.LENGTH_SHORT).show()
                        onConnectionRestored(true)
                    } else {
                        Toast.makeText(this@MainActivity, "Sigue sin conexión Wi-Fi", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Reintentar conexión")
            }
        }
    }

    sealed class Screen {
        object ConnectionChecker : Screen()
        object Login : Screen()
        object App : Screen()
    }
}