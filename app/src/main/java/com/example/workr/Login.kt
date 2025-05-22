package com.example.workr

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.workr.BuildConfig.BACKEND_BASE_URL
import kotlinx.serialization.Serializable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.workr.HTTPClientAPI
import io.ktor.client.call.body
import io.ktor.http.HttpMethod
import io.ktor.client.statement.bodyAsText
import io.ktor.client.plugins.*

@Composable
fun LoginScreen(navController: NavHostController, onRegisterClick: () -> Unit) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isEmpresa = remember { mutableStateOf(false) } // false = empleado, true = empresa

    // Variables para mensajes de error
    val emailError = remember { mutableStateOf(false) }
    val passwordError = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BlueCorners()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // Título
            Text(
                text = "Work-R",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email
            RoundedInputField(
                value = email.value,
                onValueChange = { email.value = it },
                placeholder = "Email",
                isError = emailError.value
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            RoundedInputField(
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = "Contraseña",
                isPassword = true,
                isError = passwordError.value
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector Empleado/Empresa (ahora con lógica clara)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Empleado")
                Switch(
                    checked = isEmpresa.value,
                    onCheckedChange = { isEmpresa.value = it }
                )
                Text("Empresa")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de login
            Button(
                onClick = {
                    emailError.value = email.value.isBlank()
                    passwordError.value = password.value.isBlank()

                    if (email.value.isNotBlank() && password.value.isNotBlank()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = HTTPClientAPI.makeRequest(
                                    endpoint = "auth/login",
                                    method = HttpMethod.Post,
                                    body = LoginRequest(email.value, password.value)
                                )

                                if (response.status.value == 200) {
                                    val loginResponse = response.body<LoginResponse>()

                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            navController.context,
                                            "Login exitoso: ${loginResponse.loginType}",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Navegación según tipo de usuario
                                        if (loginResponse.loginType == "company") {
                                            navController.navigate("company_profile")
                                        } else {
                                            navController.navigate("user_profile")
                                        }
                                    }
                                } else {
                                    val errorMsg = response.bodyAsText()
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            navController.context,
                                            "Error de login: $errorMsg",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        navController.context,
                                        "Excepción: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            navController.context,
                            "Por favor ingrese todos los campos.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFD9EEFF),
                    contentColor = Color(0xFF0077CC)
                )
            ) {
                Text("Login", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿Aún no estás dentro?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = R.color.blue_WorkR)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),

            ) {
                OutlinedButton(
                    onClick = { navController.navigate("register_user") },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(id = R.color.blue_WorkR)
                    ),
                    border = BorderStroke(1.dp, colorResource(id = R.color.blue_WorkR))
                ) {
                    Text("Registrar Usuario", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }

                OutlinedButton(
                    onClick = { navController.navigate("company_register") },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(id = R.color.blue_WorkR)
                    ),
                    border = BorderStroke(1.dp, colorResource(id = R.color.blue_WorkR))
                ) {
                    Text("Registrar Empresa", fontSize = 14.sp,fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun RoundedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    isError: Boolean = false
) {
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    val keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = colorResource(id = R.color.gray_WorkR)) },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE3EFF3)),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.Transparent,
            focusedBorderColor = if (isError) Color.Red else Color.Transparent,
            unfocusedBorderColor = if (isError) Color.Red else Color.Transparent,
            textColor = colorResource(id = R.color.black)
        ),
        singleLine = true
    )
}

@Composable
fun BlueCorners() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        drawPath(
            path = Path().apply {
                moveTo(0f, 0f)
                lineTo(width * 0.6f, 0f)
                lineTo(0f, height * 0.3f)
                close()
            },
            color = Color(0xFF0077CC)
        )

        drawPath(
            path = Path().apply {
                moveTo(width, height)
                lineTo(width, height * 0.7f)
                lineTo(width * 0.4f, height)
                close()
            },
            color = Color(0xFF0077CC)
        )
    }
}
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)
@Serializable
data class LoginResponse(
    val jwt: String,
    val id: String,
    val loginType: String,
    val virtualOfficeCompanyId: String,
)
