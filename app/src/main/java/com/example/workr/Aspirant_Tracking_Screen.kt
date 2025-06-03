package com.example.workr

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.statement.*
import io.ktor.http.*

// Modelo de aspirante
data class Aspirant(
    val id: Int,
    val name: String,
    val email: String? = null
)

// Petición HTTP para obtener aspirantes por vacante
suspend fun getAspirantsByVacancyId(vacancyId: String): List<Aspirant> {
    val response = HTTPClientAPI.makeRequest(
        endpoint = "job_applications/vacancy_applicants",
        method = HttpMethod.Get,
        body = mapOf("vacancyId" to vacancyId)
    )
    val responseBodyText = response.bodyAsText()
    Log.d("aspirante_debug", responseBodyText)

    return try {
        val gson = Gson()
        val listType = object : TypeToken<List<Aspirant>>() {}.type
        gson.fromJson<List<Aspirant>>(responseBodyText, listType)
    } catch (e: Exception) {
        Log.e("aspirante_debug", "Error parseando aspirantes: ${e.message}")
        emptyList()
    }
}

// Actividad principal para gestión de aspirantes
class AspirantTrackingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = intent.getStringExtra("user_id").orEmpty()
        val vacancyId = intent.getStringExtra("vacancy_id").orEmpty()

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "aspirant_tracking") {
                composable("aspirant_tracking") {
                    AspirantTrackingScreen(
                        navController = navController,
                        userId = userId,
                        vacancyId = vacancyId
                    )
                }
                composable("initial_aspirant_postulation_form") {
                    PostulacionFormScreen(
                        navController = navController,
                        loginType = "company",
                        userId = userId,
                        fromAspirantsTrackingList = "initial"
                    )
                }
                composable("contacted_aspirant_postulation_form") {
                    PostulacionFormScreen(
                        navController = navController,
                        loginType = "company",
                        userId = userId,
                        fromAspirantsTrackingList = "contacted"
                    )
                }
                composable("interview_notes") {
                    InterviewNotesScreen(navController = navController)
                }
            }
        }
    }
}

// Pantalla principal de gestión de aspirantes con tabs
@Composable
fun AspirantTrackingScreen(
    navController: NavHostController,
    userId: String,
    vacancyId: String
) {
    val tabsNavController = rememberNavController()
    val tabs = listOf(AspirantTrackingNavTabs.INITIAL, AspirantTrackingNavTabs.CONTACTED)
    val navBackStackEntry by tabsNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedTabIndex = tabs.indexOfFirst { it.route == currentRoute }.takeIf { it >= 0 } ?: 0

    var aspirantsInitial by remember { mutableStateOf<List<Aspirant>>(emptyList()) }
    var companyProfile by remember { mutableStateOf<Map<String, Any>?>(null) }

    LaunchedEffect(userId, vacancyId) {
        if (vacancyId.isNotEmpty()) {
            try {
                aspirantsInitial = getAspirantsByVacancyId(vacancyId)
                // Puedes extraer datos de empresa si el backend los devuelve
                // companyProfile = ...
            } catch (e: Exception) {
                Log.e("aspirante_debug", "Error loading aspirants: ${e.message}")
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Gestión de Aspirantes",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.black)
            )
        )

        companyProfile?.let {
            Text(
                text = "Empresa: ${it["name"] ?: "Desconocida"}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.blue_WorkR)
            )
        }

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        if (currentRoute != tab.route) {
                            tabsNavController.navigate(tab.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    text = {
                        Text(
                            text = tab.label,
                            color = colorResource(id = R.color.blue_WorkR)
                        )
                    }
                )
            }
        }

        NavHost(
            navController = tabsNavController,
            startDestination = AspirantTrackingNavTabs.INITIAL.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(AspirantTrackingNavTabs.INITIAL.route) {
                if (aspirantsInitial.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay aspirantes registrados aún",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.gray_WorkR)
                        )
                    }
                } else {
                    InitialAspirantsListScreen(
                        vacancyId = vacancyId,
                        userId = userId,
                        onFormButtonPressed = {
                            navController.navigate("initial_aspirant_postulation_form")
                        }
                    )
                }
            }

            composable(AspirantTrackingNavTabs.CONTACTED.route) {
                ContactedAspirantsListScreen(
                    userId = userId,
                    vacancyId = vacancyId,
                    onFormButtonPressed = {
                        navController.navigate("contacted_aspirant_postulation_form")
                    },
                    onInterviewButtonPressed = {
                        navController.navigate("interview_notes")
                    }
                )
            }
        }
    }
}

// Tabs disponibles
enum class AspirantTrackingNavTabs(val route: String, val label: String) {
    INITIAL("initial", "Aspirantes iniciales"),
    CONTACTED("contacted", "Aspirantes contactados")
}
