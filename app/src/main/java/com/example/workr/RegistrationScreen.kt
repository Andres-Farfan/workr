package com.example.workr

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Composable
fun RegistrationScreen(
    navController: NavHostController
) {
    // Estados locales
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }

    // Estados para errores de validación
    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var countryError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val client = remember { HttpClient(CIO) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
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

        // Contenido del formulario
        WorkRTopBar(
            navController = navController,
            isEmpleado = isEmpleado,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Join Work-R", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nombre(s)") },
                modifier = Modifier.fillMaxWidth(),
                isError = firstNameError
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido(s)") },
                modifier = Modifier.fillMaxWidth(),
                isError = lastNameError
            )
            OutlinedTextField(
                value = contact,
                onValueChange = { contact = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError
            )
            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("País") },
                modifier = Modifier.fillMaxWidth(),
                isError = countryError
            )
            OutlinedTextField(
                value = position,
                onValueChange = { position = it },
                label = { Text("Posición Actual (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    firstNameError = firstName.isBlank()
                    lastNameError = lastName.isBlank()
                    countryError = country.isBlank()
                    emailError = contact.isBlank()
                    passwordError = password.isBlank()

                    val fullName = "${firstName.trim()} ${lastName.trim()}".trim()

                    if (!firstNameError && !lastNameError && !countryError && !emailError && !passwordError && fullName.isNotBlank()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = HTTPClientAPI.makeRequest(
                                    endpoint = "users/register",
                                    method = HttpMethod.Post,
                                    body = RegistrationRequest(
                                        email = contact,
                                        password = password,
                                        country = country,
                                        fullName = fullName
                                    )
                                )

                                if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                        navController.navigate("user_profile") {
                                            popUpTo("register_screen") { inclusive = true }
                                        }
                                    }
                                } else {
                                    val errorMsg = response.bodyAsText()
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Error en registro: $errorMsg", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Excepción: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Por favor complete todos los campos correctamente.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD9EEFF),
                    contentColor = Color(0xFF0077CC)
                )
            ) {
                Text("Registrar", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

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

@Serializable
data class RegistrationRequest(
    val email: String,
    val password: String,
    val country: String,
    val fullName: String
)

