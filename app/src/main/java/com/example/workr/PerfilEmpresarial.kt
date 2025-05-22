package com.example.workr


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.navigation.NavHostController


@Composable
fun PerfilEmpresarialScreen(navController: NavHostController, isEmpleado: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Barra azul con iconos
        WorkRTopBar(
            navController = navController,
            isEmpleado = isEmpleado,
            modifier = Modifier
                .align(Alignment.CenterHorizontally) // Esquina derecha centrada verticalmente
                .padding(end = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Título FUERA de la barra azul, texto negro
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Empleos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Cerca de tu zona",
                fontSize = 14.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fondo gris claro con círculo azul centrado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp)
                .background(color = Color(0xFFDEE9ED))
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center)
                    .background(Color(0xFF0066CC), shape = CircleShape)
                    .padding(15.dp)
            )
            // Ícono opcional dentro del círculo
            Box(modifier = Modifier.align(Alignment.Center)) {
                // Agrega un ícono si lo necesitas
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Nombre y descripción
        Text("Nombre", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            "Descripción de la empresa y su función.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 26.dp, start = 16.dp, end = 16.dp)
        )

        // Sección "Sobre Nosotros"
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(34.dp),
            backgroundColor = Color(0xFFF2F8FC),
            elevation = 4.dp,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Sobre Nosotros",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Visión:",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Nuestra visión de la empresa y cuáles son las metas.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Misión:",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Nuestra misión es lograr nuestros objetivos estratégicos.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Sección "Contacto"
        Column(
            modifier = Modifier.padding(horizontal = 34.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Contacto", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    ) {
                        append("Correo: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("nombre@usuario")
                    }
                },
                modifier = Modifier.clickable { /* Acción al hacer clic */ }
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    ) {
                        append("Sitio Web: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("empresa.com.mx")
                    }
                },
                modifier = Modifier.clickable { /* Acción al hacer clic */ }
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    ) {
                        append("Ubicación: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Google Maps")
                    }
                },
                modifier = Modifier.clickable { /* Acción al hacer clic */ }
            )
        }
    }
}
