package com.example.workr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
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

    // Declaración de estados para el editor de perfil.
    var profilePictureURL by remember { mutableStateOf("") }
    val profilePictureFile = remember { mutableStateOf<File?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val contactLinks = remember { mutableStateListOf<SnapshotStateMap<String, String>>() }
    val experienceRecords = remember { mutableStateListOf<SnapshotStateMap<String, String>>() }
    val skills = remember { mutableStateListOf<String>() }
    val educationRecords = remember { mutableStateListOf<SnapshotStateMap<String, String>>() }

    // Declaración de estados para la previsualización de nuevas fotos de perfil.
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val previewPictureFile = remember { mutableStateOf<File?>(null) }

    // Launcher de actividad que permite seleccionar imágenes.
    val profilePictureSelectorLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let {
            // Se crea el archivo para almacenar la imagen en caché.
            val mimeType = context.contentResolver.getType(uri)
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            val tempFile = File.createTempFile("profile", ".$extension", context.cacheDir)

            // Se llena el archivo con datos de la imagen.
            context.contentResolver.openInputStream(uri)?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            // Se referencia al archivo desde el estado de previewPictureFile.
            previewPictureFile.value = tempFile
        }
    }

    // Obtención de datos actuales del perfil al abrir el editor.
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val body: Map<String, Any> = getProfileData(userId)

            // Obtención de la URL de imagen, nombre y descripción.
            val presentationData: Map<String, String> =
                body["presentationData"]!! as Map<String, String>
            profilePictureURL = presentationData["profilePicture"]!!
            name = presentationData["fullName"]!!
            description = presentationData["description"]!!

            // Con la URL se descarga el archivo de la imagen actual para volverlo
            // a subir en caso de que no se cambie la imagen.
            val file = saveImageAsFile(context, profilePictureURL)
            profilePictureFile.value = file

            // Obtención de datos de perfil "por listados":

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

            val educationRecordsData = body["education"]!! as List<Map<String, String>>
            educationRecords.addAll(educationRecordsData.map { original ->
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
            // Imagen de perfil
            val painter = rememberAsyncImagePainter(
                model = selectedImageUri ?: profilePictureURL,
                error = null
            )
            if (painter.state is AsyncImagePainter.State.Error) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Icono por defecto",
                    modifier = Modifier.size(128.dp).align(Alignment.CenterHorizontally),
                    colorResource(R.color.blue_WorkR)
                )
            }
            else {
                Image (
                    painter = painter,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.size(128.dp).clip(CircleShape).align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
            }

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { profilePictureSelectorLauncher.launch("image/*") }
            ) {
                Text("Elegir nueva foto de perfil")
            }

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

            Row (
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
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
                            if (previewPictureFile.value == null && profilePictureFile == null) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Debe elegir una foto de perfil", Toast.LENGTH_SHORT).show()
                                }
                                return@launch
                            }

                            if (description == "") {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Debe incluir una descripción", Toast.LENGTH_SHORT).show()
                                }
                                return@launch
                            }

                            val response = HTTPClientAPI.submitMultipartForm(
                                endpoint = "users/update_profile",
                                formFields = mapOf(
                                    "profile_picture" to (previewPictureFile.value ?: profilePictureFile.value ?: ""),
                                    "description" to description,
                                    "contactLinks" to contactLinks,
                                    "experience" to experienceRecords,
                                    "skills" to skills,
                                    "education" to educationRecords,
                                )
                            )

                            if (response.status.value == 200) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                                    navController.navigate("user_profile")
                                }
                            }
                            else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Hubo un error al actualizar el perfil, código error: ${response.status.value}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

/**
 * Función auxiliar para guardar una imagen de perfil previa como archivo en caché,
 * descargándola desde la URL indicada.
 * @param context Contexto de la aplicación en el momento de la descarga.
 * @param url URL de la imagen a descargar.
 * @return Un File que contiene la referencia a la descarga.
 */
suspend fun saveImageAsFile(context: Context, url: String): File? {
    // Se solicita la imagen.
    val loader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(url)
        .allowHardware(false) // Esto se requiere para obtener el bitmap.
        .build()

    // Se obtiene el bitmap desde el resultado de la request.
    val result = (loader.execute(request) as? SuccessResult) ?: return null
    val drawable = result.drawable as? BitmapDrawable ?: return null
    val bitmap = drawable.bitmap

    // Se obtiene la extensión de la imagen directo de la URL.
    val extension = when {
        url.endsWith(".png", ignoreCase = true) -> "png"
        url.endsWith(".webp", ignoreCase = true) -> "webp"
        else -> "jpg" // por defecto
    }

    // Se obtiene el formato a partir de la extensión.
    val format = when (extension) {
        "png" -> Bitmap.CompressFormat.PNG
        "webp" -> Bitmap.CompressFormat.WEBP
        else -> Bitmap.CompressFormat.JPEG
    }

    // Se crea y llena el archivo en caché.
    val file = File(context.cacheDir, "imagen_temp_${System.currentTimeMillis()}.$extension")
    file.outputStream().use { out ->
        bitmap.compress(format, 100, out)
    }

    return file
}