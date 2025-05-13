package com.example.workr

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.workr.BuildConfig.BACKEND_BASE_URL
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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

            Text(
                text = "Work-R",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            RoundedInputField(
                value = email.value,
                onValueChange = { email.value = it },
                placeholder = "Email"
            )

            Spacer(modifier = Modifier.height(16.dp))

            RoundedInputField(
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = "Contraseña",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val success = login(email.value, password.value)
                        if (success) {
                            onLoginClick()
                        } else {
                            Toast.makeText(
                                context,
                                "Correo o contraseña incorrectos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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

            TextButton(onClick = onRegisterClick) {
                Text(
                    text = "¿Aún no estás dentro?\n¡Crea una cuenta!",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.blue_WorkR)
                )
            }
        }
    }
}

@Composable
fun RoundedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    val visualTransformation =
        if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
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
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
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

suspend fun login(email: String, password: String): Boolean {
    val body = mapOf("email" to email, "password" to password)

    return try {
        val response = HTTPClientAPI.makeRequest(
            endpoint = "https://workr-backend.vercel.app/api/login",
            method = HttpMethod.Post,
            body = body
        )

        Log.d("Login", "Código de respuesta: ${response.status.value}")
        Log.d("Login", "Body: ${response.bodyAsText()}")

        response.status.value == 200
    } catch (e: Exception) {
        Log.e("Login", "Error: ${e.message}")
        false
    }
}

