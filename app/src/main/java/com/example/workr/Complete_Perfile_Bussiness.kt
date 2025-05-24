package com.example.workr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController


@Composable
fun CompletePerfile(
    loginType: String,
    userId: String,
    navController: NavHostController){

    var description by remember { mutableStateOf("") }
    var vision by remember { mutableStateOf("") }
    var mission by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var websiteUrl by remember { mutableStateOf("") }
    var locationUrl by remember { mutableStateOf("") }

    // Determinar si el usuario es empleado
    val isEmpleado = loginType == "employee"

    Column(modifier = Modifier.fillMaxSize()) {
        // Encabezado azul
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color(0xFF0078C1))
        )

        WorkRTopBar(
            navController = navController,
            isEmpleado = isEmpleado,
            loginType = loginType,
            userId = userId
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Comleta tu Perfil de Empresa",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción de la empresa") },
                placeholder = { Text("¿Quiénes somos? ¿Qué hacemos?") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = vision,
                onValueChange = { vision = it },
                label = { Text("Visión de la empresa") },
                placeholder = { Text("¿Hacia dónde va la empresa?") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = mission,
                onValueChange = { mission = it },
                label = { Text("Misión de la empresa") },
                placeholder = { Text("¿Cuál es su propósito?") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo de la empresa") },
                placeholder = { Text("ejemplo@empresa.com") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = websiteUrl,
                onValueChange = { websiteUrl = it },
                label = { Text("Sitio web") },
                placeholder = { Text("https://www.empresa.com") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = locationUrl,
                onValueChange = { locationUrl = it },
                label = { Text("Ubicación") },
                placeholder = { Text("https://maps.google.com/...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = { /* acción para guardar */ },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color(0xFF0078C1)),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Color(0xFF0078C1),
                    contentColor = Color.White
                )
            ) {
                androidx.compose.material.Text("Completar")
            }
            OutlinedButton(
                onClick = { /* acción para guardar */ },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color(0xFF0078C1)),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Color(0xFFD1EAFA),
                    contentColor = Color(0xFF0078C1)
                )
            ) {
                androidx.compose.material.Text("Regresar")
            }
        }
    }
}