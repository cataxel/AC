package com.example.ac_a

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.ac.APIConf.NetworkClient
import org.ac.Controller.Usuarios.UsuariosController
import com.example.ac_a.Views.AppBar
import com.example.ac_a.Views.BottomNavigationBar
import com.example.ac_a.Views.DrawerContent
import com.example.ac_a.Views.Home.Home
import com.example.ac_a.Views.Usuarios.Profile
import org.ac.Controller.Usuarios.ProfileController
import org.ac.service.Usuarios.Usuarios
import org.ac.sessionManager.interfaces.SessionManager

@Composable
fun App(sessionManager: SessionManager) {
    val usuariosService = remember { Usuarios(NetworkClient.httpClient) }
    val controller = remember { UsuariosController(usuariosService) }
    val controllerPerfil = remember { ProfileController(usuariosService) }

    var navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue =  DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    MaterialTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(
                    onNavigate = { route ->
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navController.navigate(route)
                    },
                    onLogout = {
                        sessionManager.logout()
                    }
                )
            }
        ) {
            Scaffold (
                topBar = {
                    AppBar(
                        navController.currentBackStackEntry?.destination?.route ?: "",
                        onMenuClick = { coroutineScope.launch { drawerState.open() }}
                    )
                },
                bottomBar = { BottomNavigationBar(navController) }
            ){ innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    NavHost(navController = navController, startDestination = Navegation.Home.name) {
                        composable(Navegation.Home.name) { Home() }
                        composable(Navegation.Profile.name) { Profile(
                            controllerPerfil,
                            usuarioId = sessionManager.getUserId()
                        ) }
                    }
                }

            }
        }
    }
}