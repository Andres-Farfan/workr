package com.example.workr

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController


@Composable
fun CompanyListing(
    loginType: String,
    userId: String,
    navController: NavHostController) {

    var searchText by remember { mutableStateOf("") }

    // Determinar si el usuario es empleado
    val isEmpleado = loginType == "employee"

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Encabezado

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0078C1))
                .padding(16.dp)
        )
        {
            WorkRTopBar(
                navController = navController,
                isEmpleado = isEmpleado,
                loginType = loginType,
                userId = userId
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Empleos",
                    fontSize = 24.sp,
                    color = colorResource(id = R.color.black) ,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Cerca de tu zona",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.black)
                )
            }
        }

        ItemSearchBox(
            searchText = searchText,
            onSearchChange = { searchText = it },
            onFilterLocationClick = { /* acción filtro ubicación */ },
            onFilterPositionClick = { /* acción filtro puesto */ },
            onDateSelectorClick = { /* acción selector fecha */ }
        )


        // Lista de empleos
        val jobs = List(4) { "Empresa" }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(jobs) { job ->
                JobItem(jobName = job)
            }
        }
        OutlinedButton(
            onClick = { /* acción para guardar */ },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
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

@Composable
fun JobItem(jobName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0FAFF), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF0078C1)),
            contentAlignment = Alignment.Center
        ) {
            Text("🖼️", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(text = jobName, fontWeight = FontWeight.Bold, color = Color(0xFF0078C1))
            Text("Posición Disponible", fontSize = 14.sp)
            Text("Descripción", fontSize = 12.sp)
            Text("Presencial/Remoto", fontSize = 12.sp, color = Color.Gray)
        }
    }
}
@Composable
fun ItemSearchBox(
    searchText: String,
    onSearchChange: (String) -> Unit,
    onFilterLocationClick: () -> Unit,
    onFilterPositionClick: () -> Unit,
    onDateSelectorClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFFDEE9ED), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nombre de la empresa") },
            trailingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Buscar")
            },
            singleLine = true,
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onFilterLocationClick, modifier = Modifier.weight(1f)) {
                Text("Ubicación en Ciudad, País")
            }
            OutlinedButton(onClick = onFilterPositionClick, modifier = Modifier.weight(1f)) {
                Text("Puestos disponibles")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onDateSelectorClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fecha de publicación")
        }
    }
}
