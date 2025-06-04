package com.example.workr

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InitialAspirantsListScreen(
    vacancyId: String, // Mantenido como String
    userId: String,
    onFormButtonPressed: () -> Unit
) {
    var aspirants by remember { mutableStateOf<List<Aspirant>>(emptyList()) }

    LaunchedEffect(userId, vacancyId) {
        aspirants = try {
            getAspirantsByVacancyId(vacancyId) // Llamada correcta como String
        } catch (e: Exception) {
            emptyList()
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        aspirants.forEach { aspirant ->
            AspirantListItem(
                name = aspirant.name,
                onFormButtonPressed = onFormButtonPressed
            )
        }
    }
}
