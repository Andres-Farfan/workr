package com.example.workr

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun RegistrationScreen(
    navController: NavHostController,
    isEmpleado: Boolean
) {
    // Estados locales
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo con barra azul arriba y esquinas redondeadas abajo
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val cornerSize = 80.dp.toPx()

            drawRect(
                color = Color(0xFF0078C1),
                size = Size(width, 60.dp.toPx())
            )

            val pathLeft = Path().apply {
                moveTo(0f, height - cornerSize)
                lineTo(0f, height)
                lineTo(cornerSize, height)
                close()
            }

            val pathRight = Path().apply {
                moveTo(width, height - cornerSize)
                lineTo(width, height)
                lineTo(width - cornerSize, height)
                close()
            }

            drawPath(pathLeft, color = Color(0xFFD0D8F0), style = Fill)
            drawPath(pathRight, color = Color(0xFFD0D8F0), style = Fill)
        }

        // Barra superior personalizada
        WorkRTopBar(
            navController = navController,
            isEmpleado = isEmpleado,
            modifier = Modifier
                .align(Alignment.CenterEnd) // Esquina derecha centrada verticalmente
                .padding(end = 12.dp)
        )

        // Contenido del formulario
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Join Work-R", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nombre(s)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido(s)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contact,
                onValueChange = { contact = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("País") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = position,
                onValueChange = { position = it },
                label = { Text("Posición Actual (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de registro
            Button(
                onClick = {
                    // Aquí puedes implementar la lógica de registro
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0078C1)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Registrarse", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de regresar
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD1EAFA)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Regresar", color = Color(0xFF0078C1))
            }
        }
    }
}

