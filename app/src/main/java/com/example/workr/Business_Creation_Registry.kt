package com.example.workr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class Business_Creation_Registry : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusinessCreationScreen()
        }
    }
}


@Preview
@Composable
fun BusinessCreationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .background(Color(0xFF0066CC)) // Azul
        )
        Text(
            text = "Registro de Empresa",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        var companyName by remember { mutableStateOf("") }
        var sector by remember { mutableStateOf("") }
        var employeesNumber by remember { mutableStateOf("") }
        var companyType by remember { mutableStateOf("") }

        // Campos de entrada con TextField
        TextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Nombre de la empresa") },
            modifier = Modifier.fillMaxWidth(),

        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = sector,
            onValueChange = { sector = it },
            label = { Text("Sector") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = employeesNumber,
            onValueChange = { employeesNumber = it },
            label = { Text("Número de empleados") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = companyType,
            onValueChange = { companyType = it },
            label = { Text("Tipo de empresa") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones para registrar o regresar
        Column(
            horizontalArrangement = Arrangement.spacedBy(16.dp),

            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { /* Lógica para registrar */ }) {
                Text("Registrar")
            }
            Button(onClick = { /* Lógica para regresar */ }) {
                Text("Regresar")
            }
        }
    }
}

