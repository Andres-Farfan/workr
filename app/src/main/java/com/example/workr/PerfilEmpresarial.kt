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
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .background(Color(0xFF0066CC)) // Azul
        )

        // Barra superior personalizada
        WorkRTopBar(
            navController = navController,
            isEmpleado = isEmpleado,
            modifier = Modifier
                .align(Alignment.CenterHorizontally) // Esquina derecha centrada verticalmente
                .padding(end = 12.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(color = Color(0xFFDEE9ED)) // Fondo gris claro
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp)
                .background(color = Color(0xFFDEE9ED)) // Fondo gris claro
        ) {
            // Círculo azul superpuesto
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(0xFF0066CC), shape = CircleShape) // Círculo azul
                    .align(Alignment.Center) // Centrado dentro del Box padre
                    .padding(15.dp)
            )
            // Texto o ícono dentro del círculo azul
            Box(
                modifier = Modifier
                    .align(Alignment.Center) // Misma posición que el círculo
            ) {
                // Aquí puedes añadir tu contenido como texto o ícono
                //Icon(
                    //painter = painterResource(id = R.drawable.folder), // Ícono de carpeta
                    //contentDescription = "Ícono de Carpeta",
                    //tint = Color.White,
                    //modifier = Modifier.size(30.dp)
                //)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(color = Color(0xFFFFFFFF)) // Fondo gris claro
        )
        // Nombre y descripción
        Text("Nombre", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            "Descripción de la empresa y su función.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 26.dp)
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
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            Text("Contacto", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                        append("Correo: ")
                    }
                    withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                        append("nombre@usuario")
                    }
                },
                modifier = Modifier.clickable { /* Acción al hacer clic */ }
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                        append("Sitio Web: ")
                    }
                    withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                        append("empresa.com.mx")
                    }
                },
                modifier = Modifier.clickable { /* Acción al hacer clic */ }
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                        append("Ubicación: ")
                    }
                    withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                        append("Google Maps")
                    }
                },
                modifier = Modifier.clickable { /* Acción al hacer clic */ }
            )
        }
    }
}
