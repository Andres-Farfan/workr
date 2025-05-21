package com.example.workr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavHostController

@Composable
fun VacantesScreen(navController: NavHostController, isEmpleado: Boolean) {
    val listaEncargos = listOf(
        Triple("Nombre del encargo", "Ubicación", 2),
        Triple("Nombre del encargo", "Ubicación", 4),
        Triple("Nombre del encargo", "Ubicación", 1),
        Triple("Nombre del encargo", "Ubicación", 5),
        Triple("Nombre del encargo", "Ubicación", 3),
    )

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Encabezado azul
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0078C1))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Postulados", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Disponibles", color = Color.White)
            }
        }

        WorkRTopBar(
            navController = navController,
            isEmpleado = isEmpleado,
        )

        // Lista
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listaEncargos) { (nombre, ubicacion, dias) ->
                PostuladoItem(
                    encargo = nombre,
                    ubicacion = ubicacion,
                    dias = dias,
                    postulados = (0..20).random()
                )
            }
        }
        OutlinedButton(
            onClick = { /* acción para guardar */ },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            border = BorderStroke(1.dp, Color(0xFF0078C1)),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFFD1EAFA),
                contentColor = Color(0xFF0078C1)
            )
        ) {
            Text("Regresar")
        }
    }
}
@Composable
fun PostuladoItem(encargo: String, ubicacion: String, dias: Int, postulados: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF)),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Imagen circular
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0078C1)),
                contentAlignment = Alignment.Center
            ) {

            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(encargo, fontWeight = FontWeight.Bold, color = Color(0xFF0078C1))
                Text(ubicacion)
                Text("Publicado hace $dias días")
                Text(
                    text = "Editar/Eliminar/ver postulados($postulados)",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
