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
fun NotificationsScreen(
    navController: NavHostController,
    loginType: String,
    userId: String
) {
    val isEmpleado = loginType == "employee"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Barra superior reutilizable
        WorkRTopBar(
            navController = navController,
            isEmpleado = isEmpleado,
            loginType = loginType,
            userId = userId
        )

        // Contenido de la pantalla
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = if (isEmpleado)
                    "No tienes notificaciones por el momento (Empleado)"
                else
                    "No hay notificaciones disponibles para tu empresa",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
