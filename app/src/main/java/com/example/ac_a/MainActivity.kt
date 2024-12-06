package com.example.ac_a

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ac_a.ui.theme.ACaTheme
import org.ac.APIConf.NetworkClient
import org.ac.service.Usuarios.Usuario
import org.ac.sessionManager.UserSessionManager
import org.ac.sessionManager.interfaces.SessionManager

class MainActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var usuarioService: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usuarioService = Usuario(NetworkClient.httpClient)
        sessionManager = UserSessionManager(this, NetworkClient.httpClient,usuarioService)

        if(!sessionManager.isUserLogginIn()){
            redirectToLogin()
            return
        }

        enableEdgeToEdge()
        setContent {
            ACaTheme {
                App(sessionManager)
            }
        }
    }

    private fun redirectToLogin() {
        // Redirige al usuario a la pantalla de inicio de sesión
        val intent = Intent(this, LoginActivity()::class.java)
        startActivity(intent)
        finish() // Finaliza la actividad actual para evitar volver con el botón "Atrás"
    }
}