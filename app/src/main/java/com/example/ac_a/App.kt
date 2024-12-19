package com.example.ac_a

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ac_a.Controller.Actividades.ActividadesController
import com.example.ac_a.Controller.Grupos.GruposController
import com.example.ac_a.Model.Grupos.Grupo
import com.example.ac_a.Views.Actividades.Actividad
import com.example.ac_a.Views.Actividades.crear_actializar_Actividad
import kotlinx.coroutines.launch
import org.ac.APIConf.NetworkClient
import org.ac.Controller.Usuarios.UsuariosController
import com.example.ac_a.Views.AppBar
import com.example.ac_a.Views.BottomNavigationBar
import com.example.ac_a.Views.DrawerContent
import com.example.ac_a.Views.Grupos.Grupos
import com.example.ac_a.Views.Grupos.crear_actializar_Grupo
import com.example.ac_a.Views.Home.Home
import com.example.ac_a.Views.Usuarios.Profile
import com.example.ac_a.service.Cloudinary.CloudinaryImages
import com.example.ac_a.service.Actividades.ActividadServicio
import com.example.ac_a.service.Grupos.GrupoServicio
import org.ac.APIConf.RetrofitClient
import org.ac.Controller.Usuarios.ProfileController
import org.ac.service.Usuarios.Usuarios
import org.ac.service.Usuarios.UsuariosService
import org.ac.service.Usuarios.interfaces.UsuarioApi
import org.ac.sessionManager.interfaces.SessionManager

@Composable
fun App(sessionManager: SessionManager) {
    //val usuariosService = remember { Usuarios(NetworkClient.httpClient) }
    val usuarioApi = RetrofitClient.apiService
    val usuariosService = remember { UsuariosService(usuarioApi)}
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
                containerColor = Color.White,
                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    NavHost(navController = navController, startDestination = Navegation.Home.name) {
                        composable(Navegation.Home.name) {
                            Home(navController=navController)
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
                        composable("grupos/{actividadNombre}") { backStackEntry ->
                            val actividadNombre = backStackEntry.arguments?.getString("actividadNombre")?: ""
                            Grupos(controller = controllerGrupo, actividadNombre = actividadNombre, navController = navController)
                        }
                        composable("crear_actializar_Actividad?guidActividad={guidActividad}&nombre={nombre}&descripcion={descripcion}") {
                            val guidActividad = it.arguments?.getString("guidActividad")
                            val nombre = it.arguments?.getString("nombre")
                            val descripcion = it.arguments?.getString("descripcion")
                            crear_actializar_Actividad(
                                navController = navController, guidActividad = guidActividad.toString(), nombreA = nombre.toString(), descripcionA = descripcion.toString()
                            )
                        }
                        composable("crear_actializar_Grupo?id={id}&guid={guid}&descripcion={descripcion}&ubicacion={ubicacion}&hora_inicial={hora_inicial}&hora_final={hora_final}&fecha_inicial={fecha_inicial}&fecha_final={fecha_final}&capacidad={capacidad}&usuario_nombre={usuario_nombre}&actividad_descripcion={actividad_descripcion}") {
                            val id = it.arguments?.getString("id")
                            val guid = it.arguments?.getString("guid")
                            val descripcion = it.arguments?.getString("descripcion")
                            val ubicacion = it.arguments?.getString("ubicacion")
                            val hora_inicial = it.arguments?.getString("hora_inicial")
                            val hora_final = it.arguments?.getString("hora_final")
                            val fecha_inicial = it.arguments?.getString("fecha_inicial")
                            val fecha_final = it.arguments?.getString("fecha_final")
                            val capacidad = it.arguments?.getString("capacidad")
                            val usuario_nombre = it.arguments?.getString("usuario_nombre")
                            val actividad_descripcion = it.arguments?.getString("actividad_descripcion")
                            crear_actializar_Grupo(
                                grupoActualizar = Grupo(id=id!!.toInt(), guid = guid.toString(), usuario_nombre = usuario_nombre.toString(), actividad_descripcion = actividad_descripcion.toString(), descripcion = descripcion.toString(), ubicacion = ubicacion.toString(), hora_inicial = hora_inicial.toString(), hora_final = hora_final.toString(), fecha_inicial = fecha_inicial.toString(), fecha_final = fecha_final.toString(), capacidad = capacidad!!.toInt()),
                                navController = navController
                            )
                        }


                    }
                }
            }
        }
    }
}
