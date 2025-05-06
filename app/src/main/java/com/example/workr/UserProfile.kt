package com.example.workr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProfileViewScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        // Rectángulo azul superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(colorResource(id = R.color.blue_WorkR))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(16.dp)) {
            ProfileHeaderCard()
            ContactSection()
            ExperienceSection()
            SkillsSection()
            StudiesSection()
        }
    }
}

@Composable
fun ProfileHeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de perfil
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.blue_WorkR))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Nombre Apellido Apellido",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.blue_WorkR)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Una increíble descripción que sirva como primera impresión del profesionista.",
                fontSize = 14.sp,
                color = colorResource(id = R.color.gray_WorkR)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ContactSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Contacto", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("RedSocial1: nombre@usuario", color = colorResource(id = R.color.blue_WorkR))
            Text("RedSocial2: nombre_usuario", color = colorResource(id = R.color.blue_WorkR))
            Text("RedSocial3: nombre.usuario", color = colorResource(id = R.color.blue_WorkR))
            Text("Número de teléfono: 123-456-7890", color = colorResource(id = R.color.blue_WorkR))
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ExperienceSection() {
    Text("Experiencia", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Posición", fontSize = 14.sp, color = colorResource(id = R.color.blue_WorkR), fontWeight = FontWeight.Bold)
            Text("Empresa", fontSize = 14.sp, color = colorResource(id = R.color.gray_WorkR))
            Text("Ene 2024 - Dic 2025", fontSize = 12.sp, color = colorResource(id = R.color.gray_WorkR))
            Text(
                "Una descripción de las tareas desempeñadas en el puesto",
                fontSize = 12.sp,
                color = colorResource(id = R.color.gray_WorkR)
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SkillsSection() {
    Text("Habilidades", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    Text("✓ Una habilidad competitiva", fontSize = 14.sp, color = colorResource(id = R.color.blue_WorkR))
    Text("✓ Una habilidad competitiva", fontSize = 14.sp, color = colorResource(id = R.color.blue_WorkR))
    Text("✓ Una habilidad competitiva", fontSize = 14.sp, color = colorResource(id = R.color.blue_WorkR))
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun StudiesSection() {
    Text("Estudios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo para ícono decorativo
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.blue_WorkR))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text("Título o Certificado", fontSize = 14.sp, color = colorResource(id = R.color.blue_WorkR), fontWeight = FontWeight.Bold)
                Text("Institución", fontSize = 14.sp, color = colorResource(id = R.color.gray_WorkR))
                Text("Ene 2024 - Dic 2025", fontSize = 12.sp, color = colorResource(id = R.color.gray_WorkR))
                Text(
                    "Una descripción de las habilidades adquiridas",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gray_WorkR)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview
@Composable
fun ProfileViewScreenPreview() {
    ProfileViewScreen()
}