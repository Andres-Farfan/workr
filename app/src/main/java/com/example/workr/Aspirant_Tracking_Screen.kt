package com.example.workr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Ventana del Sistema Gestor de Aspirantes usada para controlar el
 * flujo de los aspirantes registrados en una vacante.
 * @param globalNavController Controlador de la navegación global de la app.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AspirantTrackingScreen(
    globalNavController: NavHostController,
    loginType: String,
    userId: String
) {
    val tabsNavController = rememberNavController()
    val startTab = AspirantTrackingNavTabs.INITIAL
    var selectedTab by rememberSaveable { mutableStateOf(startTab) }
    val isEmpleado = loginType == "user"

    WorkRScaffold(
        navController = globalNavController,
        loginType = loginType,
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
            PrimaryTabRow(selectedTabIndex = selectedTab.ordinal) {
                AspirantTrackingNavTabs.entries.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTab.ordinal == index,
                        onClick = {
                            tabsNavController.navigate(route = tab.route)
                            selectedTab = tab
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
                startDestination = startTab.route,
                modifier = Modifier.fillMaxSize()
            ) {
                AspirantTrackingNavTabs.entries.forEach { destination ->
                    composable(destination.route) {
                        when (destination) {
                            AspirantTrackingNavTabs.INITIAL -> InitialAspirantsListScreen(
                                onFormButtonPressed = {
                                    globalNavController.navigate("initial_aspirant_postulation_form")
                                }
                            )
                            AspirantTrackingNavTabs.CONTACTED -> ContactedAspirantsListScreen(
                                onFormButtonPressed = {
                                    globalNavController.navigate("contacted_aspirant_postulation_form")
                                },
                                onInterviewButtonPressed = {
                                    globalNavController.navigate("interview_notes")
                                }
                            )
                        }
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