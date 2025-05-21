package com.example.workr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun NotificationsScreen(navController: NavHostController, isEmpleado : Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Barra azul con la barra de navegación
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color(0xFF007AFF)),
            contentAlignment = Alignment.CenterStart
        ) {
            WorkRTopBar(navController = navController, isEmpleado = isEmpleado)
        }

        // Contenido vacío por ahora
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Por ejemplo:
            // Text("No hay notificaciones", modifier = Modifier.align(Alignment.Center))
        }
    }
}