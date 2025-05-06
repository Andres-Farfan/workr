package com.example.workr

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RegistrationContent(
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    contact: String,
    onContactChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    country: String,
    onCountryChange: (String) -> Unit,
    position: String,
    onPositionChange: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Canvas with rounded corners and color
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val cornerSize = 80.dp.toPx()

            // Draw the blue top bar
            drawRect(
                color = Color(0xFF0078C1), // Blue color for the top bar
                size = Size(width, 60.dp.toPx())
            )

            // Draw the rounded corners at the bottom
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

        // Column for form inputs
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Text(text = "Join Work-R", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            // Input fields
            OutlinedTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                label = { Text("Nombre(s)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                label = { Text("Apellido(s)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contact,
                onValueChange = onContactChange,
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = country,
                onValueChange = onCountryChange,
                label = { Text("País") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = position,
                onValueChange = onPositionChange,
                label = { Text("Posición Actual (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Register Button
            Button(
                onClick = { /* Registration action */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0078C1)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Registrarse", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Back Button
            Button(
                onClick = { /* Back action */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD1EAFA)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Regresar", color = Color(0xFF0078C1))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreen() {
    RegistrationContent(
        firstName = "Juan",
        onFirstNameChange = {},
        lastName = "Pérez",
        onLastNameChange = {},
        contact = "email@example.com",
        onContactChange = {},
        password = "1234",
        onPasswordChange = {},
        country = "Mexico",
        onCountryChange = {},
        position = "Engineer",
        onPositionChange = {}
    )
}