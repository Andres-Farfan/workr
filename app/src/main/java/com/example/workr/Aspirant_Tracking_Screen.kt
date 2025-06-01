package com.example.workr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * Activity propia para el sistema gestor de aspirantes que permite
 * darle su propia navegación independiente de la principal.
 */
class AspirantTrackingActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se obtienen datos del intent para la activity del sistema gestor
        // de aspirantes.
        val intent = intent
        val userId = intent.getStringExtra("user_id").orEmpty()

        setContent {
            // Este controlador de navegación servirá de manera local
            // en el sistema gestor de aspirantes.
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "aspirant_tracking") {
                composable("aspirant_tracking") { AspirantTrackingScreen(navController) }

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

/**
 * Ventana del Sistema Gestor de Aspirantes usada para controlar el
 * flujo de los aspirantes registrados en una vacante.
 * @param navController Controlador de la navegación local del sistema de aspirantes.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AspirantTrackingScreen(
    navController: NavHostController,
) {
    val tabsNavController = rememberNavController()

    val tabs = listOf("initial", "contacted")
    val navBackStackEntry by tabsNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedTabIndex = tabs.indexOf(currentRoute).takeIf { it >= 0 } ?: 0

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Título centrado
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

        // Pestañas
        PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
            AspirantTrackingNavTabs.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        if (currentRoute != tab.route) {
                            tabsNavController.popBackStack()
                            tabsNavController.navigate(route = tab.route) {
                                launchSingleTop = true
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

        // Contenido de cada pestaña
        NavHost(
            navController = tabsNavController,
            startDestination = "initial",
            modifier = Modifier.fillMaxSize()
        ) {
            AspirantTrackingNavTabs.entries.forEach { destination ->
                composable(destination.route) {
                    when (destination) {
                        AspirantTrackingNavTabs.INITIAL -> InitialAspirantsListScreen(
                            onFormButtonPressed = {
                                navController.navigate("initial_aspirant_postulation_form")
                            }
                        )
                        AspirantTrackingNavTabs.CONTACTED -> ContactedAspirantsListScreen(
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
    }
}


/**
 * Clase enumeradora que lista las posibles pestañas para navegar en el
 * sistema de gestión de aspirantes.
 */
enum class AspirantTrackingNavTabs(
    val route: String,
    val label: String
) {
    INITIAL("initial", "Aspirantes iniciales"),
    CONTACTED("contacted", "Aspirantes contactados")
}