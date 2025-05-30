package com.example.workr

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController

@Composable
fun ProfileEditScreen(
    loginType: String,
    userId: String,
    navController: NavHostController
) {
    val isEmpleado = loginType == "user"
    var description by remember { mutableStateOf(TextFieldValue()) }
    var contactLink by remember { mutableStateOf(TextFieldValue()) }
    var experiences by remember { mutableStateOf(mutableListOf<ExperienceState>()) }

    WorkRScaffold(
        navController = navController,
        loginType = loginType,
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // para poder scrollear si es necesario
                .padding(16.dp)
        ) {

            // Ya no necesitas el Box azul ni WorkRTopBar porque el scaffold lo maneja

            // Imagen de perfil
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0078C1))
                    .align(Alignment.CenterHorizontally)
                    .clickable { /* acción para cambiar foto */ }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Seleccionar foto de perfil",
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nombre Apellido Apellido",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Descripción", fontSize = 16.sp)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Una increíble descripción que sirva como su primera impresión") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Contacto", fontSize = 16.sp)
            OutlinedTextField(
                value = contactLink,
                onValueChange = { contactLink = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enlace de red social") },
                trailingIcon = {
                    IconButton(onClick = { contactLink = TextFieldValue("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Eliminar")
                    }
                }
            )

            OutlinedTextField(
                value = contactLink,
                onValueChange = { contactLink = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Número de teléfono") },
                trailingIcon = {
                    IconButton(onClick = { contactLink = TextFieldValue("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Eliminar")
                    }
                }
            )

            OutlinedButton(
                onClick = { /* agregar contacto */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Color.White,
                    contentColor = Color(0xFF0078C1)
                ),
                border = BorderStroke(1.dp, colorResource(id = R.color.blue_WorkR))
            ) {
                Text("+ Agregar Enlace de Contacto")
            }

            experiences.forEachIndexed { index, experience ->
                ExperienceItem(
                    experience = experience,
                    onRemove = {
                        experiences = experiences.toMutableList().apply { removeAt(index) }
                    }
                )
            }

            OutlinedButton(
                onClick = { /* agregar número de teléfono */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Color.White,
                    contentColor = Color(0xFF0078C1)
                ),
                border = BorderStroke(1.dp, colorResource(id = R.color.blue_WorkR))
            ) {
                Text("+ Agregar Número de Teléfono")
            }

            experiences.forEachIndexed { index, experience ->
                ExperienceItem(
                    experience = experience,
                    onRemove = {
                        experiences = experiences.toMutableList().apply { removeAt(index) }
                    }
                )
            }

            OutlinedButton(
                onClick = {
                    experiences = (experiences + ExperienceState()).toMutableList()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Color.White,
                    contentColor = Color(0xFF0078C1)
                ),
                border = BorderStroke(1.dp, Color(0xFF0078C1))
            ) {
                Text("+ Agregar Experiencia")
            }
        }
    }
}

@Composable
fun ExperienceItem(
    experience: ExperienceState,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 400.dp) // altura máxima del bloque con scroll
            .background(Color(0xFFEDEDED), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Elemento de Experiencia",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Close, contentDescription = "Eliminar")
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = experience.position,
                onValueChange = { experience.position = it },
                label = { Text("Posición") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = experience.company,
                onValueChange = { experience.company = it },
                label = { Text("Empresa") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = experience.startDate,
                onValueChange = { experience.startDate = it },
                label = { Text("Fecha inicio") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = experience.endDate,
                onValueChange = { experience.endDate = it },
                label = { Text("Fecha fin") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = experience.activities,
                onValueChange = { experience.activities = it },
                label = { Text("Descripción de actividades") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Nuevo modelo para manejar estados dinámicos
class ExperienceState {
    var position by mutableStateOf("")
    var company by mutableStateOf("")
    var startDate by mutableStateOf("")
    var endDate by mutableStateOf("")
    var activities by mutableStateOf("")
}
