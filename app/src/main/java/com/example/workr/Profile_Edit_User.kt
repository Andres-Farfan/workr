package com.example.workr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun ProfileEditScreen(
    loginType: String,
    userId: String,
    navController: NavHostController
) {
    val isEmpleado = loginType == "user"

    val context = LocalContext.current

    var profilePictureURL by remember { mutableStateOf("") }
    val profilePictureFile = remember { mutableStateOf<File?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val contactLinks = remember { mutableStateListOf<SnapshotStateMap<String, String>>() }
    val experienceRecords = remember { mutableStateListOf<SnapshotStateMap<String, String>>() }
    val skills = remember { mutableStateListOf<String>() }
    val educationRecords = remember { mutableStateListOf<SnapshotStateMap<String, String>>() }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val body: Map<String, Any> = getProfileData(userId)

            val presentationData: Map<String, String> =
                body["presentationData"]!! as Map<String, String>
            profilePictureURL = presentationData["profilePicture"]!!
            name = presentationData["fullName"]!!
            description = presentationData["description"]!!

//            val file = saveImageAsFile(context, profilePictureURL)
            val file = saveImageAsFile(context, "https://res.cloudinary.com/dtdwsuiiy/image/upload/v1748838591/vl2e0ffcnlwmecr05wtp.jpg")
            profilePictureFile.value = file

            val contactLinksData = body["contactLinks"]!! as List<Map<String, String>>
            contactLinks.addAll(contactLinksData.map { original ->
                mutableStateMapOf<String, String>().apply {
                    putAll(original)
                }
            })

            val experienceRecordsData = body["experience"]!! as List<Map<String, String>>
            experienceRecords.addAll(experienceRecordsData.map { original ->
                mutableStateMapOf<String, String>().apply {
                    putAll(original)
                }
            })

            skills.addAll(body["skills"]!! as List<String>)

            val studyRecordsData = body["education"]!! as List<Map<String, String>>
            educationRecords.addAll(studyRecordsData.map { original ->
                mutableStateMapOf<String, String>().apply {
                    putAll(original)
                }
            })
        }
    }

    WorkRScaffold(
        navController = navController,
        loginType = loginType,
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // para poder scrollear si es necesario
                .padding(16.dp)
        ) {

            // Ya no necesitas el Box azul ni WorkRTopBar porque el scaffold lo maneja

            // Imagen de perfil
            val painter = rememberAsyncImagePainter(
                model = profilePictureURL,
                error = null
            )
            if (painter.state is AsyncImagePainter.State.Error) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Icono por defecto",
                    modifier = Modifier.size(128.dp),
                    colorResource(R.color.blue_WorkR)
                )
            }
            else {
                Image (
                    painter = painter,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.size(128.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
//            Box(
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .background(Color(0xFF0078C1))
//                    .align(Alignment.CenterHorizontally)
//                    .clickable { /* acción para cambiar foto */ }
//            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Seleccionar foto de perfil",
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = name,
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Descripción", fontSize = 16.sp)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Una increíble descripción que sirva como su primera impresión") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Contacto", fontSize = 16.sp)
            Column {
                contactLinks.forEachIndexed { index, contactLink ->
                    Column (
                        modifier = Modifier
                            .border(BorderStroke(1.dp, Color.Black))
                            .padding(horizontal = 8.dp),
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Datos de contacto")
                            IconButton(
                                onClick = { contactLinks.removeAt(index) }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    "Borrar",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        TextField(
                            value = contactLink["platform"]!!,
                            onValueChange = { contactLink["platform"] = it },
                            placeholder = { Text("Plataforma de contacto") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = contactLink["link"]!!,
                            onValueChange = { contactLink["link"] = it },
                            placeholder = { Text("Referencia de contacto") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    androidx.compose.material.Divider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            OutlinedButton(
                onClick = {
                    contactLinks.add(mutableStateMapOf("platform" to "", "link" to ""))
                },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, colorResource(id = R.color.blue_WorkR))
            ) {
                Text("+ Agregar Datos de contacto")
            }

            Text("Experiencia")
            Column {
                experienceRecords.forEachIndexed { index, record ->
                    Column (
                        modifier = Modifier
                            .border(BorderStroke(1.dp, Color.Black))
                            .padding(horizontal = 8.dp),
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Elemento de experiencia")
                            IconButton(
                                onClick = { experienceRecords.removeAt(index) }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    "Borrar",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        TextField(
                            value = record["position"]!!,
                            onValueChange = { record["position"] = it },
                            placeholder = { Text("Posición") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = record["company"]!!,
                            onValueChange = { record["company"] = it },
                            placeholder = { Text("Empresa") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = record["startDate"]!!,
                            onValueChange = { record["startDate"] = it },
                            placeholder = { Text("Fecha de inicio") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = record["endDate"]!!,
                            onValueChange = { record["endDate"] = it },
                            placeholder = { Text("Fecha de fin") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = record["description"]!!,
                            onValueChange = { record["description"] = it },
                            placeholder = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    androidx.compose.material.Divider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            OutlinedButton(
                onClick = {
                    experienceRecords.add(mutableStateMapOf(
                        "position" to "",
                        "company" to "",
                        "startDate" to "",
                        "endDate" to "",
                        "description" to ""
                    ))
                },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color(0xFF0078C1))
            ) {
                Text("+ Agregar Experiencia")
            }

            Text("Habilidades", fontSize = 16.sp)
            Column {
                skills.forEachIndexed { index, skill ->
                    Column (
                        modifier = Modifier
                            .border(BorderStroke(1.dp, Color.Black))
                            .padding(horizontal = 8.dp),
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Habilidad")
                            IconButton(
                                onClick = { skills.removeAt(index) }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    "Borrar",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        TextField(
                            value = skill,
                            onValueChange = { skill -> skills[index] = skill },
                            placeholder = { Text("Habilidad competitiva") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    androidx.compose.material.Divider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            OutlinedButton(
                onClick = {
                    skills.add("")
                },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, colorResource(id = R.color.blue_WorkR))
            ) {
                Text("+ Agregar Habilidad")
            }

            Text("Estudios")
            Column {
                educationRecords.forEachIndexed { index, record ->
                    Column (
                        modifier = Modifier
                            .border(BorderStroke(1.dp, Color.Black))
                            .padding(horizontal = 8.dp),
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Elemento de estudios")
                            IconButton(
                                onClick = { educationRecords.removeAt(index) }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    "Borrar",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        TextField(
                            value = record["title"]!!,
                            onValueChange = { record["title"] = it },
                            placeholder = { Text("Título o Certificado") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = record["organization"]!!,
                            onValueChange = { record["organization"] = it },
                            placeholder = { Text("Institución") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = record["startDate"]!!,
                            onValueChange = { record["startDate"] = it },
                            placeholder = { Text("Fecha de inicio") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = record["endDate"]!!,
                            onValueChange = { record["endDate"] = it },
                            placeholder = { Text("Fecha de fin") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = record["description"]!!,
                            onValueChange = { record["description"] = it },
                            placeholder = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    androidx.compose.material.Divider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            OutlinedButton(
                onClick = {
                    educationRecords.add(mutableStateMapOf(
                        "title" to "",
                        "organization" to "",
                        "startDate" to "",
                        "endDate" to "",
                        "description" to ""
                    ))
                },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color(0xFF0078C1))
            ) {
                Text("+ Agregar Estudios")
            }

            Row {
                OutlinedButton(
                    onClick = {
                        navController.navigate("user_profile")
                    }
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            Log.d("debug", "File exists: ${profilePictureFile.value?.exists()}, size: ${profilePictureFile.value?.length()}")

                            val response = HTTPClientAPI.submitMultipartForm(
                                endpoint = "users/update_profile",
                                formFields = mapOf(
                                    "profile_picture" to profilePictureFile.value!!,
                                    "description" to description,
                                    "contactLinks" to contactLinks,
                                    "experience" to experienceRecords,
                                    "skills" to skills,
                                    "education" to educationRecords,
                                )
                            )

                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, response.bodyAsText(), Toast.LENGTH_SHORT).show()
                            }

                            Log.d("debug", response.bodyAsText())
                        }
                    }
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

suspend fun saveImageAsFile(context: Context, url: String): File? {
    val loader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(url)
        .allowHardware(false) // Necesario para obtener Bitmap
        .build()

    val result = (loader.execute(request) as? SuccessResult)?.drawable ?: return null

    // Convertir a Bitmap
    val bitmap = (result as BitmapDrawable).bitmap

    // Guardar como archivo
    val file = File(context.cacheDir, "imagen_temp_${System.currentTimeMillis()}.jpg")
    file.outputStream().use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }

    return file
}



@Composable
fun ExperienceItem(
    experience: ExperienceState,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 400.dp) // altura máxima del bloque con scroll
            .background(Color(0xFFEDEDED), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Elemento de Experiencia",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Close, contentDescription = "Eliminar")
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = experience.position,
                onValueChange = { experience.position = it },
                label = { Text("Posición") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = experience.company,
                onValueChange = { experience.company = it },
                label = { Text("Empresa") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = experience.startDate,
                onValueChange = { experience.startDate = it },
                label = { Text("Fecha inicio") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = experience.endDate,
                onValueChange = { experience.endDate = it },
                label = { Text("Fecha fin") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = experience.activities,
                onValueChange = { experience.activities = it },
                label = { Text("Descripción de actividades") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Nuevo modelo para manejar estados dinámicos
class ExperienceState {
    var position by mutableStateOf("")
    var company by mutableStateOf("")
    var startDate by mutableStateOf("")
    var endDate by mutableStateOf("")
    var activities by mutableStateOf("")
}
