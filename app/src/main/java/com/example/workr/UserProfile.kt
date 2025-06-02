package com.example.workr

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import io.ktor.client.call.body
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

suspend fun getProfileData(userId: String): Map<String, Any> {
    val response = HTTPClientAPI.makeRequest(
        endpoint = "users/profile/$userId",
        method = HttpMethod.Get
    )

    return response.body() as Map<String, Any>
}

@Composable
fun ProfileViewScreen(
    loginType: String,
    userId: String,
    navController: NavHostController
) {
    val isEmpleado = loginType == "user"

    var profilePictureURL by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var contactLinks = remember { mutableStateListOf<Map<String, String>>() }
    var experienceRecords = remember { mutableStateListOf<Map<String, String>>() }
    var skills = remember { mutableStateListOf<String>() }
    var educationRecords = remember { mutableStateListOf<Map<String, String>>() }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val body: Map<String, Any> = getProfileData(userId)

            val presentationData: Map<String, String> =
                body["presentationData"]!! as Map<String, String>
            profilePictureURL = presentationData["profilePicture"]!!
            name = presentationData["fullName"]!!
            description = presentationData["description"]!!

            contactLinks.addAll(body["contactLinks"]!! as List<Map<String, String>>)
            experienceRecords.addAll(body["experience"]!! as List<Map<String, String>>)
            skills.addAll(body["skills"]!! as List<String>)
            educationRecords.addAll(body["education"]!! as List<Map<String, String>>)
        }
    }

    WorkRScaffold(
        navController = navController,
        loginType = loginType,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // 🔹 Padding para no tapar el contenido con la top bar
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ProfileHeaderCard(profilePictureURL, name, description)
            ContactSection(contactLinks)
            ExperienceSection(experienceRecords)
            SkillsSection(skills)
            StudiesSection(educationRecords)
        }
    }
}

@Composable
fun ProfileHeaderCard(profilePictureURL: String, name: String, description: String) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val painter = rememberAsyncImagePainter(
                model = profilePictureURL,
                error = null
            )
            if (painter.state is AsyncImagePainter.State.Error) {
                Icon(
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.blue_WorkR)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = colorResource(id = R.color.gray_WorkR)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ContactSection(contactLinks: List<Map<String, String>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Contacto", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                contactLinks.forEach { contactLink ->
                    Text("${contactLink["platform"]!!}: ${contactLink["link"]!!}", color = colorResource(id = R.color.blue_WorkR))
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ExperienceSection(experienceRecords: List<Map<String, String>>) {
    Text("Experiencia", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    Column {
        experienceRecords.forEach { record ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text("${record["position"]!!}", fontSize = 14.sp, color = colorResource(id = R.color.blue_WorkR), fontWeight = FontWeight.Bold)
                Text("${record["company"]!!}", fontSize = 14.sp, color = colorResource(id = R.color.gray_WorkR))
                Text("${record["startDate"]!!} - ${record["endDate"]!!}", fontSize = 12.sp, color = colorResource(id = R.color.gray_WorkR))
                Text(
                    "${record["description"]!!}",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gray_WorkR)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SkillsSection(skills: List<String>) {
    Text("Habilidades", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    Column {
        skills.forEach { skill ->
            Text("✓ $skill", fontSize = 14.sp, color = colorResource(id = R.color.blue_WorkR))
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun StudiesSection(educationRecords: List<Map<String, String>>) {
    Text("Estudios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    Column {
        educationRecords.forEach { record ->
            Column {
                Text("${record["title"]!!}", fontSize = 14.sp, color = colorResource(id = R.color.blue_WorkR), fontWeight = FontWeight.Bold)
                Text("${record["organization"]!!}", fontSize = 14.sp, color = colorResource(id = R.color.gray_WorkR))
                Text("${record["startDate"]!!} - ${record["endDate"]!!}", fontSize = 12.sp, color = colorResource(id = R.color.gray_WorkR))
                Text(
                    "${record["description"]}",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gray_WorkR)
                )
            }
        }
    }
}
