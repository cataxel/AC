package com.example.ac_a.Views.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ac_a.Navegation
import com.example.ac_a.R
import kotlinx.coroutines.delay

@Composable
fun Home(navController: NavController) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF82B1FF), Color(0xFF40C4FF)),
        startY = 0f,
        endY = 1000f
    )

    val images = listOf(
        R.drawable.ajedrez,
        R.drawable.futbol,
        R.drawable.basquet,
        R.drawable.danza,
        R.drawable.banda_de_guerra
    )

    var currentIndex by remember { mutableStateOf(0) }
    val totalImages = images.size

    LaunchedEffect(currentIndex) {
        delay(3000)
        currentIndex = (currentIndex + 1) % totalImages
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Bienvenido a Halcones AC",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(24.dp))

            ImageCarousel(
                images = images,
                currentIndex = currentIndex,
                onImageChange = { currentIndex = it }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Tu aplicación para gestionar actividades y grupos.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Invitamos a la comunidad del TecNM Campus Jiquilpan a conocer más sobre nuestras actividades disponibles en el semestre.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = { navController.navigate(Navegation.Actividades.name) },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2979FF))
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Saber más",
                    tint = Color.White,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Saber más",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ImageCarousel(
    images: List<Int>,
    currentIndex: Int,
    onImageChange: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    val newIndex = when {
                        dragAmount > 0 -> (currentIndex - 1).takeIf { it >= 0 } ?: images.size - 1
                        dragAmount < 0 -> (currentIndex + 1) % images.size
                        else -> currentIndex
                    }
                    onImageChange(newIndex)
                    change.consume()
                }
            }
    ) {
        Image(
            painter = painterResource(id = images[currentIndex]),
            contentDescription = "Image Carousel",
            modifier = Modifier.fillMaxSize()
        )
    }
}
