package com.example.workr

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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

// --- Pantalla Principal ---
@Composable
fun PostulacionFormScreen(
    navController: NavHostController,
    loginType: String,
    userId: String,
    fromAspirantsTrackingList: String? = null
) {
    val isEmpleado = loginType == "employee"

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

        if (fromAspirantsTrackingList == null) {
            WorkRTopBar(
                navController = navController,
                isEmpleado = isEmpleado,
                loginType = loginType,
                userId = userId
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            BlueTopBar()

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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                LabelWithInput("Nombre completo:", "Nombre completo(Primero el apellido)", nombre, fieldsEnabled)
                LabelWithInput("Número de teléfono:", "Teléfono", telefono, fieldsEnabled)
                LabelWithInput("Correo electrónico:", "Correo electrónico", correo, fieldsEnabled)
                LabelWithInput("¿Último nivel de estudio alcanzado?", "Nivel alcanzado", nivelEstudio, fieldsEnabled)
                LabelWithInput("¿Último puesto de empleo? (opcional)", "Último empleo", ultimoEmpleo, fieldsEnabled)
                LabelWithInput("Nivel de experiencia", "Nivel alcanzado", experiencia, fieldsEnabled)
                LabelWithInput("¿Cuáles son tus principales habilidades técnicas?", "Principales técnicas", habilidades, fieldsEnabled)
                LabelWithInput("¿Qué herramientas o softwares dominas?", "Herramientas dominadas", herramientas, fieldsEnabled)
                LabelWithInput("¿Por qué quieres trabajar en nuestra empresa?", "Razón", razonIngreso, fieldsEnabled)
                LabelWithInput("¿Puedes compartir un portafolio o ejemplos de tu trabajo?", "Razón", portafolio, fieldsEnabled)

                Spacer(modifier = Modifier.height(24.dp))

                if (fromAspirantsTrackingList == null) {
                    Button(
                        onClick = { /* Acción al enviar */ },
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
                        OutlinedButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Text("Regresar")
                        }

                        if (fromAspirantsTrackingList == "initial") {
                            Button(onClick = {}) {
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

// --- Campo con etiqueta + input ---
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

// --- Barra superior azul ---
@Composable
fun BlueTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(colorResource(id = R.color.blue_WorkR))
    )
}