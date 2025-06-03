package com.example.workr

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.example.workr.HTTPClientAPI
import io.ktor.client.call.body
import io.ktor.http.HttpMethod
import androidx.compose.runtime.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun PerfilEmpresarialScreen(
    navController: NavHostController,
    loginType: String,
    userId: String
) {
    var companyProfile by remember { mutableStateOf<Map<String, Any>?>(null) }

    LaunchedEffect(userId) {
        companyProfile = getCompanyProfileData(userId)
    }

    WorkRScaffold(
        navController = navController,
        loginType = loginType
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Imagen de perfil (perfil redondo)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .background(color = Color(0xFFDEE9ED)),
                contentAlignment = Alignment.Center
            ) {
                val profilePictureUrl = companyProfile?.get("profilePicture") as? String
                if (profilePictureUrl != null) {
                    AsyncImage(
                        model = profilePictureUrl,
                        contentDescription = "Imagen perfil empresa",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Si no hay imagen, un cuadro azul redondo vacío
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(Color(0xFF0066CC), shape = CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Nombre de la empresa
            Text(
                text = companyProfile?.get("name") as? String ?: "Nombre desconocido",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Descripción corta
            Text(
                text = companyProfile?.get("description") as? String ?: "",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                backgroundColor = Color(0xFFF2F8FC),
                elevation = 4.dp
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
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        companyProfile?.get("vision") as? String ?: "No disponible",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Misión:",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        companyProfile?.get("mission") as? String ?: "No disponible",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contacto (usa la lista de contactLinks)
            val contactLinks = companyProfile?.get("contactLinks") as? List<Map<String, String>>

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Contacto", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(6.dp))

                if (contactLinks != null) {
                    contactLinks.forEach { contact ->
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                                    append("${contact["platform"] ?: "Plataforma"}: ")
                                }
                                withStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                                    append(contact["link"] ?: "Sin enlace")
                                }
                            },
                            modifier = Modifier.clickable { /* Puedes agregar acción para abrir link */ }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                } else {
                    Text("No hay información de contacto disponible.")
                }
                //boton editar perfil
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        navController.navigate("complete_profile_company")
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
                    Text("Editar perfil")
                }
            }
        }
    }
}
suspend fun getCompanyProfileData(companyId: String): Map<String, Any> {
    val response = HTTPClientAPI.makeRequest(
        endpoint = "companies/profile/$companyId",
        method = HttpMethod.Get
    )
    // Si sabes que es un Map, puedes convertirlo después
    return response.body() as Map<String, Any>
}
