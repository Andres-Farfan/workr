package com.example.workr

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.compose.runtime.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton


@Composable
fun BusinessCreationScreen(navController: NavHostController, isEmpleado: Boolean) {
    var companyName by remember { mutableStateOf("") }
    var sector by remember { mutableStateOf("") }
    var employeesNumber by remember { mutableStateOf("") }
    var companyType by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("workr_prefs", Context.MODE_PRIVATE)

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {

        // Menú en la esquina superior derecha
        WorkRTopBar(
            navController = navController,
            isEmpleado = isEmpleado,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp, end = 12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Registra tu Empresa",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        // Logo de empresa (círculo)
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFF0078C1))
                .align(Alignment.CenterHorizontally)
                .clickable { /* Abrir selector de imagen */ }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Seleccionar logo de empresa",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = companyName,
                onValueChange = { companyName = it },
                label = { Text("Nombre de la empresa") },
                placeholder = { Text("Ej: WorkR S.A.") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = sector,
                onValueChange = { sector = it },
                label = { Text("Sector") },
                placeholder = { Text("Ej: Tecnología, Salud...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = employeesNumber,
                onValueChange = { employeesNumber = it },
                label = { Text("Número de empleados") },
                placeholder = { Text("Ej: 50") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = companyType,
                onValueChange = { companyType = it },
                label = { Text("Tipo de empresa") },
                placeholder = { Text("Ej: Startup, PyME...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    if (companyName.isNotBlank() && sector.isNotBlank()) {
                        with(sharedPref.edit()) {
                            putBoolean("has_company", true)
                            apply()
                        }
                        navController.navigate("company_profile")
                    } else {
                        // Mostrar error o Snackbar si lo deseas
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color(0xFF0078C1)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF0078C1),
                    contentColor = Color.White
                )
            ) {
                Text("Registrar")
            }
            OutlinedButton(
                onClick = {
                    // Acción para regresar, por ejemplo:
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color(0xFF0078C1)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFD1EAFA),
                    contentColor = Color(0xFF0078C1)
                )
            ) {
                Text("Regresar")
            }
        }
    }
}


