package com.example.workr

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController

@Composable
fun JobDetailScreen(navController: NavHostController, isEmpleado: Boolean) {
    Box(modifier = Modifier.fillMaxSize()) {
        RectangleCorners()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFF0078C1))
            ) {
                WorkRTopBar(
                    navController = navController,
                    isEmpleado = isEmpleado,
                )
            }

            Text(
                text = "Posición de Vacante",
                fontSize = 18.sp,
                color = colorResource(id = R.color.blue_WorkR),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                // Empresa + icono

                OutlinedTextField(
                    value = "Nombre de empresa",
                    onValueChange = {},
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = colorResource(id = R.color.blue_WorkR),
                                    shape = CircleShape
                                )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = R.color.gray_WorkR),
                        unfocusedBorderColor = colorResource(id = R.color.gray_WorkR),
                        textColor = colorResource(id = R.color.black)
                    ),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.black)
                    ),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                InfoRow(R.drawable.calendar_icon, "10 de marzo de 2025")
                InfoRow(R.drawable.location_icon, "Ubicación de la oferta")

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tipo de Posición",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.blue_WorkR),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorResource(id = R.color.white))
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.blue_WorkR),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Descripción", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                    text = "Una descripción acerca de las principales actividades de esta posición que deje claro al solicitante el tipo de trabajo que estará realizando.",
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Habilidades preferibles", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                BulletPoint("Habilidad competitiva")
                BulletPoint("Habilidad competitiva")
                BulletPoint("Habilidad competitiva")

                Spacer(modifier = Modifier.height(16.dp))

                Text("Horario laboral", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Lunes a Viernes\n09:00 AM a 05:00 PM", fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { /* Acción */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(45.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(id = R.color.blue_WorkR)
                    ),
                    border = BorderStroke(1.dp, colorResource(id = R.color.blue_WorkR)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Regresar",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                Button(
                    onClick = { /* Lógica de aplicar */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .padding(start = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.blue_WorkR),
                        contentColor = colorResource(id = R.color.white)
                    )
                ) {
                    Text("Aplicar")
                }
            }
        }
    }
}

@Composable
fun InfoRow(drawableId: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = drawableId),
            contentDescription = null,
            tint = Color.Unspecified, // para mantener los colores originales del vector
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, fontSize = 13.sp)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun BulletPoint(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text("✓", color = colorResource(id = R.color.black), modifier = Modifier.padding(end = 6.dp))
        Text(text, fontSize = 13.sp)
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun RectangleCorners() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val triangleWidth = width * 0.25f
        val triangleHeight = height * 0.15f

        // Izquierda inferior
        drawPath(
            path = Path().apply {
                moveTo(0f, height)
                lineTo(0f, height - triangleHeight)
                lineTo(triangleWidth, height)
                close()
            },
            color = Color(0x552196F3) // Azul translúcido
        )

        // Derecha inferior
        drawPath(
            path = Path().apply {
                moveTo(width, height)
                lineTo(width, height - triangleHeight)
                lineTo(width - triangleWidth, height)
                close()
            },
            color = Color(0x552196F3)
        )
    }
}
