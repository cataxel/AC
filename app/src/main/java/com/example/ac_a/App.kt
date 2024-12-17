package com.example.ac_a

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ac_a.Controller.Actividades.ActividadesController
import com.example.ac_a.Controller.Grupos.GruposController
import com.example.ac_a.Views.Actividades.Actividad
import kotlinx.coroutines.launch
import org.ac.APIConf.NetworkClient
import org.ac.Controller.Usuarios.UsuariosController
import com.example.ac_a.Views.AppBar
import com.example.ac_a.Views.BottomNavigationBar
import com.example.ac_a.Views.DrawerContent
import com.example.ac_a.Views.Grupos.Grupos
import com.example.ac_a.Views.Home.Home
import com.example.ac_a.Views.Usuarios.Profile
import com.example.ac_a.service.Cloudinary.CloudinaryImages
import com.example.ac_a.service.Actividades.ActividadServicio
import com.example.ac_a.service.Grupos.GrupoServicio
import org.ac.Controller.Usuarios.ProfileController
import org.ac.service.Usuarios.Usuarios
import org.ac.sessionManager.interfaces.SessionManager

@Composable
fun App(sessionManager: SessionManager) {
    val usuariosService = remember { Usuarios(NetworkClient.httpClient) }
    val actividadService = remember { ActividadServicio(NetworkClient.httpClient) }
    val cloudinaryService = remember { CloudinaryImages() }
    val grupoService = remember { GrupoServicio(NetworkClient.httpClient) }
    val usuariosController = remember { UsuariosController(usuariosService) }
    val profileController = remember { ProfileController(usuariosService, cloudinaryService) }
    val controllerActividad = remember { ActividadesController(actividadService) }
    val controllerGrupo = remember { GruposController(grupoService) }

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    MaterialTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(
                    onNavigate = { route ->
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate(route)
                    },
                    onLogout = {
                        sessionManager.logout()
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    AppBar(
                        navController.currentBackStackEntry?.destination?.route ?: "",
                        onMenuClick = { coroutineScope.launch { drawerState.open() } }
                    )
                },
                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    NavHost(navController = navController, startDestination = Navegation.Home.name) {
                        composable(Navegation.Home.name) {
                            Home()
                        }
                        composable(Navegation.Profile.name) {
                            Profile(
                                controller = profileController,
                                usuarioId = sessionManager.getUserId()
                            )
                        }
                        composable(Navegation.Actividades.name) {
                            Actividad(
                                controller = controllerActividad,
                                usuarioId = sessionManager.getUserId(),
                                navController = navController
                            )
                        }
                        composable("grupos/{actividadId}") { backStackEntry ->
                            val actividadId = backStackEntry.arguments?.getString("actividadId")?.toInt() ?: 0
                            Grupos(controller = controllerGrupo, actividadId = actividadId)
                        }
                    }
                }
            }
        }
    }
}
