package com.example.workr

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PostulacionFormScreen(
    navController: NavHostController,
    loginType: String,
    vacancyId: String,
    fromAspirantsTrackingList: String? = null
) {
    val isEmpleado = loginType == "user"
    val context = LocalContext.current

    val nombre = remember { mutableStateOf("") }
    val telefono = remember { mutableStateOf("") }
    val correo = remember { mutableStateOf("") }
    val nivelEstudio = remember { mutableStateOf("") }
    val ultimoEmpleo = remember { mutableStateOf("") }
    val experiencia = remember { mutableStateOf("") }
    val habilidades = remember { mutableStateOf("") }
    val herramientas = remember { mutableStateOf("") }
    val razonIngreso = remember { mutableStateOf("") }
    val portafolio = remember { mutableStateOf("") }

    val fieldsEnabled = (fromAspirantsTrackingList == null)
    val aspirantTrackingListMode = fromAspirantsTrackingList != null

    WorkRScaffold(
        navController = navController,
        loginType = loginType,
        fromAspirantsTrackingList = aspirantTrackingListMode
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val cornerSize = 80.dp.toPx()

                if (!aspirantTrackingListMode) {
                    drawRect(
                        color = Color(0xFF0078C1),
                        size = Size(width, 60.dp.toPx())
                    )
                }

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
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {

                Text(
                    text = "Formulario para la\npostulación",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )

                LabelWithInput("Nombre completo:", "Nombre completo (Primero el apellido)", nombre, fieldsEnabled)
                LabelWithInput("Número de teléfono:", "Teléfono", telefono, fieldsEnabled)
                LabelWithInput("Correo electrónico:", "Correo electrónico", correo, fieldsEnabled)
                LabelWithInput("¿Último nivel de estudio alcanzado?", "Nivel alcanzado", nivelEstudio, fieldsEnabled)
                LabelWithInput("¿Último puesto de empleo? (opcional)", "Último empleo", ultimoEmpleo, fieldsEnabled)
                LabelWithInput("Nivel de experiencia", "Nivel alcanzado", experiencia, fieldsEnabled)
                LabelWithInput("¿Cuáles son tus principales habilidades técnicas?", "Principales técnicas", habilidades, fieldsEnabled)
                LabelWithInput("¿Qué herramientas o softwares dominas?", "Herramientas dominadas", herramientas, fieldsEnabled)
                LabelWithInput("¿Por qué quieres trabajar en nuestra empresa?", "Razón", razonIngreso, fieldsEnabled)
                LabelWithInput("¿Puedes compartir un portafolio o ejemplos de tu trabajo?", "Link o descripción", portafolio, fieldsEnabled)

                Spacer(modifier = Modifier.height(24.dp))

                if (!aspirantTrackingListMode) {
                    Button(
                        onClick = {
                            // Validaciones
                            if (nombre.value.isBlank() || correo.value.isBlank() || telefono.value.isBlank() ||
                                nivelEstudio.value.isBlank() || experiencia.value.isBlank() ||
                                habilidades.value.isBlank() || herramientas.value.isBlank() ||
                                razonIngreso.value.isBlank()
                            ) {
                                Toast.makeText(context, "Completa todos los campos obligatorios.", Toast.LENGTH_LONG).show()
                                return@Button
                            }

                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo.value).matches()) {
                                Toast.makeText(context, "Correo electrónico no válido.", Toast.LENGTH_LONG).show()
                                return@Button
                            }

                            val postRequest = PostulacionRequest(
                                contactEmail = correo.value.trim(),
                                phoneNumber = telefono.value.trim(),
                                highestEducationLevel = nivelEstudio.value.trim(),
                                experience = experiencia.value.trim(),
                                hardSkills = herramientas.value.trim(),
                                softSkills = habilidades.value.trim(),
                                applicationReason = razonIngreso.value.trim(),
                                portfolioLink = portafolio.value.trim(),
                                vacancyId = userId
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val response = HTTPClientAPI.makeRequest(
                                        endpoint = "job_applications/register",
                                        method = HttpMethod.Post,
                                        body = postRequest
                                    )
                                    if (response.status.isSuccess()) {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "Postulación enviada exitosamente.", Toast.LENGTH_LONG).show()

                                            // Limpiar campos
                                            nombre.value = ""
                                            telefono.value = ""
                                            correo.value = ""
                                            nivelEstudio.value = ""
                                            ultimoEmpleo.value = ""
                                            experiencia.value = ""
                                            habilidades.value = ""
                                            herramientas.value = ""
                                            razonIngreso.value = ""
                                            portafolio.value = ""

                                            navController.popBackStack()
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "Error al enviar: ${response.status.value}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(25.dp)),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.cian_WorkR),
                            contentColor = colorResource(id = R.color.blue_WorkR)
                        )
                    ) {
                        Text(
                            text = "Enviar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                navController.popBackStack()
                            },
                            border = BorderStroke(1.dp, colorResource(id = R.color.cian_WorkR)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = colorResource(id = R.color.blue_WorkR)
                            )
                        ) {
                            Text("Regresar")
                        }

                        if (fromAspirantsTrackingList == "initial") {
                            OutlinedButton(
                                onClick = {
                                    // Aquí puedes realizar la lógica de agendar cita
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            val response = HTTPClientAPI.makeRequest(
                                                endpoint = "job_applications/register_interview",
                                                method = HttpMethod.Post,
                                                body = mapOf(
                                                    "companyId" to userId.toIntOrNull(), // Asegura que sea Int
                                                    "applicantEmail" to correo.value.trim()
                                                )
                                            )
                                            withContext(Dispatchers.Main) {
                                                if (response.status.isSuccess()) {
                                                    Toast.makeText(
                                                        context,
                                                        "Cita agendada correctamente.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Error: ${response.status.value}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        } catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(
                                                    context,
                                                    "Error inesperado: ${e.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                },
                                border = BorderStroke(1.dp, colorResource(id = R.color.blue_WorkR)),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = colorResource(id = R.color.blue_WorkR)
                                )
                            ) {
                                Text("Agendar cita")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun LabelWithInput(label: String, placeholder: String, state: MutableState<String>, enabled: Boolean) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = colorResource(id = R.color.black)
        )
        OutlinedTextField(
            value = state.value,
            enabled = enabled,
            onValueChange = { state.value = it },
            placeholder = {
                Text(text = placeholder, color = colorResource(id = R.color.gray_WorkR))
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
            textStyle = TextStyle.Default.copy(fontSize = 14.sp),
            keyboardOptions = KeyboardOptions.Default
        )
    }
}

data class PostulacionRequest(
    val contactEmail: String,
    val phoneNumber: String,
    val highestEducationLevel: String,
    val experience: String,
    val hardSkills: String,
    val softSkills: String,
    val applicationReason: String,
    val portfolioLink: String,
    val vacancyId: String
)
