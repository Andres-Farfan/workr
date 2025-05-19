package com.example.workr

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
// Anotación para utilizar la clase PrimaryTabRow, experimental de Material 3.
@OptIn(ExperimentalMaterial3Api::class)
fun AspirantTrackingScreen(globalNavController: NavHostController) {
    // Configuración de variables necesarias para el Navigation Host de las pestañas.
    val tabsNavController = rememberNavController()
    val startTab = AspirantTrackingNavTabs.INITIAL

    // Se guardará como estado el índice de enumeración (ordinal) de la pestaña abierta
    // para actualizar la apariencia de pestaña seleccionada.
    var selectedTab by rememberSaveable { mutableStateOf(startTab) }

    Column {
        PrimaryTabRow (selectedTabIndex = selectedTab.ordinal) {
            AspirantTrackingNavTabs.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab.ordinal == index,
                    onClick = {
                        tabsNavController.navigate(route = tab.route)
                        selectedTab = tab
                    },
                    text = {
                        Text(
                            text = tab.label
                        )
                    }
                )
            }
        }
        NavHost(
            tabsNavController,
            startDestination = startTab.route
        ) {
            AspirantTrackingNavTabs.entries.forEach { destination ->
                composable(destination.route) {
                    when(destination) {
                        AspirantTrackingNavTabs.INITIAL -> InitialAspirantsListScreen(
                            onFormButtonPressed = {
                                globalNavController.navigate("initial_aspirant_postulation_form")
                            }
                        )
                        AspirantTrackingNavTabs.CONTACTED -> ContactedAspirantsListScreen(
                            onFormButtonPressed = {
                                globalNavController.navigate("contacted_aspirant_postulation_form")
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