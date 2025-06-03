package com.example.workr

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import android.util.Base64
import android.webkit.MimeTypeMap
import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.io.ByteArrayOutputStream
import java.io.File

@Composable
fun BusinessCreationScreen(navController: NavHostController) {
    var companyName by remember { mutableStateOf("") }
    var sector by remember { mutableStateOf("") }
    var employeesNumber by remember { mutableStateOf("") }
    var companyType by remember { mutableStateOf("") }
    var adminEmail by remember { mutableStateOf("") }
    var adminPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("workr_prefs", Context.MODE_PRIVATE)
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
    ) {
        WorkRTopBar(
            navController = navController,
            loginType = "company",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(65.dp)
        )

        val imageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            selectedImageUri = uri
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Registra tu Empresa",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0078C1))
                    .clickable { imageLauncher.launch("image/*") }
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                selectedImageUri?.let { uri ->
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bitmap = inputStream?.use { BitmapFactory.decodeStream(it) }
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Logo seleccionado",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Seleccionar logo de empresa",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Nombre de la empresa") },
                    placeholder = { Text("Ej: WorkR S.A.") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0078C1),
                        unfocusedBorderColor = Color(0xFF0078C1),
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = sector,
                    onValueChange = { sector = it },
                    label = { Text("Sector") },
                    placeholder = { Text("Ej: Tecnología") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0078C1),
                        unfocusedBorderColor = Color(0xFF0078C1),
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = employeesNumber,
                    onValueChange = { employeesNumber = it },
                    label = { Text("Número de empleados") },
                    placeholder = { Text("Ej: 50") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0078C1),
                        unfocusedBorderColor = Color(0xFF0078C1),
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = companyType,
                    onValueChange = { companyType = it },
                    label = { Text("Tipo de empresa") },
                    placeholder = { Text("Ej: PyME") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0078C1),
                        unfocusedBorderColor = Color(0xFF0078C1),
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = adminEmail,
                    onValueChange = { adminEmail = it },
                    label = { Text("Correo del administrador") },
                    placeholder = { Text("Ej: admin@empresa.com") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0078C1),
                        unfocusedBorderColor = Color(0xFF0078C1),
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = adminPassword,
                    onValueChange = { adminPassword = it },
                    label = { Text("Contraseña") },
                    placeholder = { Text("Ingresa la contraseña") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0078C1),
                        unfocusedBorderColor = Color(0xFF0078C1),
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
                OutlinedButton(
                    onClick = {
                        if (companyName.isBlank() || sector.isBlank() || employeesNumber.toIntOrNull() == null ||
                            adminEmail.isBlank() || adminPassword.isBlank() || selectedImageUri == null
                        ) {
                            Toast.makeText(
                                context,
                                "Por favor, completa todos los campos y selecciona una imagen",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@OutlinedButton
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val tempFile = selectedImageUri?.let { uri ->
                                    val inputStream = context.contentResolver.openInputStream(uri)
                                    val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())

                                    File.createTempFile("profile_picture", ".$extension", context.cacheDir).apply {
                                        outputStream().use { fileOut ->
                                            inputStream?.copyTo(fileOut)
                                        }
                                    }
                                }

                                if (tempFile == null) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Error al procesar la imagen", Toast.LENGTH_SHORT).show()
                                    }
                                    return@launch
                                }

                                val formFields = mapOf<String, Any>(
                                    "profile_picture" to tempFile,
                                    "name" to companyName,
                                    "commercialSector" to sector,
                                    "employeeCount" to employeesNumber.toInt().toString(),
                                    "type" to companyType,
                                    "adminEmail" to adminEmail,
                                    "adminPassword" to adminPassword
                                )


                                val response = HTTPClientAPI.submitMultipartForm(
                                    endpoint = "companies/register",
                                    method = HttpMethod.Post,
                                    formFields = formFields
                                )

                                if (response.status.value == 200) {
                                    val responseBody = response.bodyAsText()
                                    withContext(Dispatchers.Main) {
                                        sharedPref.edit().putBoolean("has_company", true).apply()
                                        Toast.makeText(
                                            context,
                                            "Registro exitoso: $responseBody",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate("company_profile")
                                    }
                                } else {
                                    val errorText = response.bodyAsText()
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Error: $errorText",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, Color(0xFF0078C1)),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF0078C1),
                        contentColor = Color.White
                    )
                ) {
                    Text("Registrar")
                }

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, Color(0xFF0078C1)),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFD1EAFA),
                        contentColor = Color(0xFF0078C1)
                    )
                ) {
                    Text("Regresar")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Función para redimensionar la imagen manteniendo proporción
fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    val bitmapRatio = width.toFloat() / height.toFloat()
    val newWidth: Int
    val newHeight: Int

    if (bitmapRatio > 1) {
        newWidth = maxSize
        newHeight = (maxSize / bitmapRatio).toInt()
    } else {
        newHeight = maxSize
        newWidth = (maxSize * bitmapRatio).toInt()
    }

    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
}
@Serializable
data class CompanyRegisterRequest(
    val profile_picture: String = "",
    val name: String,
    val adminEmail: String,
    val adminPassword: String,
    val commercialSector: String,
    val employeeCount: Int,
    val type: String
)

@Serializable
data class CompanyRegisterResponse(
    val jwt: String,
    val companyId: String,
    val message: String
)