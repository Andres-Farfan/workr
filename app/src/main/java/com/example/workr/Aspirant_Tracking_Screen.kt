package com.example.workr

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Ventana del Sistema Gestor de Aspirantes usada para controlar el
 * flujo de los aspirantes registrados en una vacante.
 */
@Composable
// Anotación para utilizar la clase PrimaryTabRow, experimental de Material 3.
@OptIn(ExperimentalMaterial3Api::class)
fun AspirantTrackingScreen() {
    // Configuración de variables necesarias para Navigation Host.
    val navController = rememberNavController()
    val startDestination = AspirantTrackingNavDestination.CONTACTED

    // Se guardará como estado el índice de enumeración (ordinal) de la pestaña abierta
    // para actualizar la apariencia de pestaña seleccionada.
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Column {
        PrimaryTabRow (selectedTabIndex = selectedDestination) {
            AspirantTrackingNavDestination.entries.forEachIndexed { index, destination ->
                Tab(
                    selected = selectedDestination == index,
                    onClick = {
                        navController.navigate(route = destination.route)
                        selectedDestination = index
                    },
                    text = {
                        Text(
                            text = destination.label
                        )
                    }
                )
            }
        }
        NavHost(
            navController,
            startDestination = startDestination.route
        ) {
            AspirantTrackingNavDestination.entries.forEach { destination ->
                composable(destination.route) {
                    when(destination) {
                        AspirantTrackingNavDestination.INITIAL -> InitialAspirantsListScreen()
                        AspirantTrackingNavDestination.CONTACTED -> ContactedAspirantsListScreen()
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
enum class AspirantTrackingNavDestination(
    val route: String,
    val label: String
) {
    INITIAL("initial", "Aspirantes iniciales"),
    CONTACTED("contacted", "Aspirantes contactados")
}