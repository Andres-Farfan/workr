package com.example.workr

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavHostController

@Composable
fun CreateJobScreen(navController: NavHostController, isEmpleado: Boolean) {
    val scrollState = rememberScrollState()

    var position by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var days by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val skills = remember { mutableStateListOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        WorkRTopBar(navController = navController, isEmpleado = isEmpleado)

        // Título centrado debajo de la barra
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Creación de vacante",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    SectionTitle("Datos generales")
                    LabeledTextField("Posición de vacante", position) { position = it }
                    Spacer(Modifier.height(8.dp))
                    LabeledTextField("Ubicación de la oferta", location) { location = it }
                    Spacer(Modifier.height(8.dp))
                    LabeledTextField("Días laborales", days) { days = it }
                    Spacer(Modifier.height(8.dp))
                    LabeledTextField("Horario de turno", schedule) { schedule = it }

                    Spacer(Modifier.height(16.dp))
                    SectionTitle("Descripción")
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        placeholder = {
                            Text(
                                "Una descripción de las responsabilidades y tareas desempeñadas en este puesto que den un entendimiento suficiente a los aspirantes"
                            )
                        }
                    )

                    Spacer(Modifier.height(16.dp))
                    SectionTitle("Habilidades preferibles")
                    skills.forEachIndexed { index, skill ->
                        OutlinedTextField(
                            value = skill,
                            onValueChange = { skills[index] = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Habilidad competitiva") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    if (index in skills.indices) skills.removeAt(index)
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Eliminar")
                                }
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    OutlinedButton(
                        onClick = { skills.add("") },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Color(0xFF0078C1)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color.White,
                            contentColor = Color(0xFF0078C1)
                        )
                    ) {
                        Text("+ Agregar Habilidad")
                    }

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { /* Cancel logic */ },
                            border = BorderStroke(1.dp, Color(0xFF0078C1)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                backgroundColor = Color.White,
                                contentColor = Color(0xFF0078C1)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = { /* Publish logic */ },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0078C1)),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        ) {
                            Text("Publicar", color = Color.White)
                        }
                    }

                    Spacer(Modifier.height(16.dp))
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
                    Spacer(Modifier.height(16.dp))
                }

                SimpleScrollbar(
                    scrollState = scrollState,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
fun LabeledTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(label) }
    )
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SimpleScrollbar(scrollState: ScrollState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(4.dp)
            .background(Color.LightGray.copy(alpha = 0.3f))
    ) {
        val proportion = if (scrollState.maxValue != 0) {
            scrollState.value.toFloat() / scrollState.maxValue.toFloat()
        } else 0f

        val thumbHeight = 60.dp
        val offsetY = with(LocalDensity.current) {
            (proportion * scrollState.maxValue.toFloat()).dp.coerceIn(0.dp, 240.dp)
        }

        Box(
            modifier = Modifier
                .offset(y = offsetY)
                .width(4.dp)
                .height(thumbHeight)
                .background(Color.DarkGray, shape = RoundedCornerShape(2.dp))
        )
    }
}


