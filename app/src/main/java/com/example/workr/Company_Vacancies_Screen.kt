package com.example.workr

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

/**
 * Ventana que lista las vacantes ofertadas por la empresa propia.
 * @param globalNavController Controlador de la navegación de la barra superior.
 * @param loginType Tipo de login diferenciando al usuario de empresa que revisa
 * su lista de vacantes, para mostrar la barra superior apropiada.
 * @param userId Id de referencia pasado en la navegación.
 */
@Composable
fun CompanyVacanciesScreen(
    globalNavController: NavHostController,
    loginType: String,
    userId: String
) {
    WorkRScaffold(
        navController = globalNavController,
        loginType = loginType
    ) { innerPadding ->
        Column {
            Text(
                "Vacantes activas",
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn (
                modifier = Modifier.weight(1.0f)
            ) {
                items (10) { item ->
                    CompanyVacancyItem(userId = userId)
                }
            }
        }
    }
}