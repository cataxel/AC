package com.example.ac_a.Views


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ac_a.Navegation
import com.example.ac_a.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentScreen: String,
    onMenuClick: () -> Unit
){
    TopAppBar(
        colors = topAppBarColors(
            containerColor = Purple80
        ),
        title = {
            Text(
                text = when (currentScreen){
                    Navegation.Home.name -> "Inicio"
                    Navegation.Profile.name -> "Perfil"
                    else -> ""
                }
            )
        },
        navigationIcon = {
          IconButton(onClick = {onMenuClick()} ){
              Icon(Icons.Default.Menu, contentDescription = "Menu")
          }
        },
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.systemBars)

    )
}

@Composable
fun BottomNavigationBar(navController: NavController){
    BottomAppBar(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.systemBars),
        actions = {
            IconButton(
                onClick = {navController.navigate(Navegation.Home.name)}
            ) { Icon(Icons.Default.Home, contentDescription = "Inicio") }
            IconButton(
                onClick = {navController.navigate(Navegation.Profile.name)}
            ) { Icon(Icons.Default.Person, contentDescription = "Perfil") }
        }
    )
}


@Composable
fun DrawerContent(onNavigate: (String) -> Unit, onLogout: () -> Unit){
    Box(modifier = Modifier.fillMaxSize()) {
        // Contenido de navegación
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Text(
                text = "Navegación",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            DrawerItem(
                label = "Perfil",
                onClick = { onNavigate(Navegation.Profile.name) }
            )
        }

        // Botón para cerrar sesión, fijado en la parte inferior
        Button(
            onClick = { onLogout() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "Cerrar sesión")
        }
    }
}

@Composable
fun DrawerItem(label: String, onClick: () -> Unit) {
    Text(
        text = label,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        style = MaterialTheme.typography.bodyLarge
    )
}