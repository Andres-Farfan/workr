package com.example.workr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material3.Text

@Composable
fun VirtualOfficeScreen(
    navController: NavHostController,
    loginType: String,
    userId: String
) {
    WorkRScaffold(
        navController = navController,
        loginType = loginType,
    ) { paddingValues ->
        val isEmpleado = loginType == "user"

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = if (isEmpleado)
                    "Bienvenido a tu Oficina Virtual (Empleado)"
                else
                    "Accede a la Oficina Virtual de tu empresa",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


