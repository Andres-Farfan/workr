package com.tuapp.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.workr.Company_Payment
import com.example.workr.EmployeesCharts
import com.example.workr.VacanciesCharts

@Composable
fun CompanyInfoScreen() {
    val context = LocalContext.current

    Scaffold(
        containerColor = Color(0xFFF5FAFC)
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                OptionCard(
                    title = "Pago de Mensualidad",
                    description = "Administra o renueva tu suscripción mensual para seguir accediendo a todas las funcionalidades de WorkR.",
                    icon = Icons.Default.AccountBox,
                    onClick = {
                        context.startActivity(Intent(context, Company_Payment::class.java))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                OptionCard(
                    title = "Gráficas de Aspirantes",
                    description = "Visualiza estadísticas detalladas sobre aspirantes, entrevistas y contrataciones.",
                    icon = Icons.Default.Call,
                    onClick = {
                        context.startActivity(Intent(context, VacanciesCharts::class.java))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                OptionCard(
                    title = "Gráficas de Empleados",
                    description = "Explora datos relevantes sobre el desempeño, asistencia y crecimiento de tus empleados.",
                    icon = Icons.Default.AccountBox,
                    onClick = {
                        context.startActivity(Intent(context, EmployeesCharts::class.java))
                    }
                )
            }
        }
    }
}

@Composable
fun OptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White, Color(0xFFE3F2FD))
                    )
                )
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF1565C0),
                    modifier = Modifier
                        .size(36.dp)
                        .padding(end = 12.dp)
                )
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF1A237E)
            )
        }
    }
}
