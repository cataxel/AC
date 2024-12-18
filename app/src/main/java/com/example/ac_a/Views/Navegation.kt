package com.example.ac_a.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ac_a.Navegation
import com.example.ac_a.ui.theme.Blue40
import com.example.ac_a.ui.theme.Blue80
import com.example.ac_a.ui.theme.LightBlue80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(currentScreen: String, onMenuClick: () -> Unit) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = Blue80
        ),
        title = {
            Text(
                text = when (currentScreen) {
                    Navegation.Home.name -> "Inicio"
                    Navegation.Profile.name -> "Perfil"
                    else -> ""
                }
            )
        },
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        modifier = Modifier.fillMaxWidth() // Asegura que ocupe toda la anchura
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth() // Asegura que ocupe toda la anchura
            .background(Blue80),
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigate(Navegation.Home.name) }
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Inicio",
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(
                    onClick = { navController.navigate(Navegation.Profile.name) }
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Perfil",
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(
                    onClick = { navController.navigate(Navegation.Actividades.name) }
                ) {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = "Actividades",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    )
}


@Composable
fun DrawerContent(onNavigate: (String) -> Unit, onLogout: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(vertical = 34.dp).background(Color.White)) {
        // Contenido de navegación
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Text(
                text = "<< Navegación",
                style = MaterialTheme.typography.titleLarge,
                color = LightBlue80,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Divider(color = LightBlue80) // Separador de color azul con opacidad
            Spacer(modifier = Modifier.height(16.dp))
            DrawerItem(
                label = "Inicio",
                onClick = { onNavigate(Navegation.Home.name) }
            )
            DrawerItem(
                label = "Perfil",
                onClick = { onNavigate(Navegation.Profile.name) }
            )
            DrawerItem(
                label = "Actividades",
                onClick = { onNavigate(Navegation.Actividades.name) }
            )
        }

        // Botón para cerrar sesión, fijado en la parte inferior
        Button(
            onClick = { onLogout() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue80)
        ) {
            Text(
                text = "Cerrar sesión",
                color = Color.White
            )
        }
    }
}

@Composable
fun DrawerItem(label: String, onClick: () -> Unit) {
    Text(
        text = label,
        color = LightBlue80,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        style = MaterialTheme.typography.bodyLarge
    )
}
