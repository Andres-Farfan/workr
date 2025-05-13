package com.example.workr

import androidx.compose.foundation.Canvas
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
import com.example.workr.BuildConfig.BACKEND_BASE_URL
import io.ktor.http.HttpMethod
import kotlinx.coroutines.launch

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
    onPositionChange: (String) -> Unit,
    onRegisterClick: (UserRegisterRequest) -> Unit,
    onBackClick: () -> Unit,
    responseText: String
) {
    Box(modifier = Modifier.fillMaxSize()) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Text(text = "Join Work-R", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

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

            if (responseText.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = responseText,
                    color = if (responseText.startsWith("Registro exitoso")) Color.Green else Color.Red
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val user = UserRegisterRequest(
                        firstName = firstName,
                        lastName = lastName,
                        contact = contact,
                        password = password,
                        country = country,
                        position = position
                    )
                    onRegisterClick(user)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0078C1)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Registrarse", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD1EAFA)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Regresar", color = Color(0xFF0078C1))
            }
        }
    }
}

@Composable
fun RegistrationScreen(onBackClick: () -> Unit = {}) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    var responseText by remember { mutableStateOf("") }

    RegistrationContent(
        firstName = firstName,
        onFirstNameChange = { firstName = it },
        lastName = lastName,
        onLastNameChange = { lastName = it },
        contact = contact,
        onContactChange = { contact = it },
        password = password,
        onPasswordChange = { password = it },
        country = country,
        onCountryChange = { country = it },
        position = position,
        onPositionChange = { position = it },
        responseText = responseText,
        onBackClick = onBackClick,
        onRegisterClick = {
            coroutineScope.launch {
                try {
                    val response = HTTPClientAPI.makeRequest(
                        endpoint = "https://workr-backend.vercel.app/api/register",
                        method = HttpMethod.Post,
                        body = it
                    )
                    responseText = "Registro exitoso: ${response.status}"
                } catch (e: Exception) {
                    responseText = "Error al registrar: ${e.message}"
                }
            }
        }
    )
}
