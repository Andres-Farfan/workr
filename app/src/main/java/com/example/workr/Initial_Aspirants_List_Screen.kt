package com.example.workr

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Sub-ventana de lista de aspirantes iniciales en una vacante
 * en el Sistema Gestor de Aspirantes.
 * @param onFormButtonPressed Callback usado cuando se presiona el botón de consulta de formulario.
 */
@Composable
fun InitialAspirantsListScreen(onFormButtonPressed: () -> Unit) {
    Column (
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AspirantListItem(name = "Nombre del aspirante", onFormButtonPressed = onFormButtonPressed)
        AspirantListItem(name = "Nombre del aspirante", onFormButtonPressed = onFormButtonPressed)
        AspirantListItem(name = "Nombre del aspirante", onFormButtonPressed = onFormButtonPressed)
        AspirantListItem(name = "Nombre del aspirante", onFormButtonPressed = onFormButtonPressed)
        AspirantListItem(name = "Nombre del aspirante", onFormButtonPressed = onFormButtonPressed)
        AspirantListItem(name = "Nombre del aspirante", onFormButtonPressed = onFormButtonPressed)
        AspirantListItem(name = "Nombre del aspirante", onFormButtonPressed = onFormButtonPressed)
        AspirantListItem(name = "Nombre del aspirante", onFormButtonPressed = onFormButtonPressed)
        AspirantListItem(name = "Nombre del aspirante", onFormButtonPressed = onFormButtonPressed)
        AspirantListItem(name = "Nombre del aspirante", onFormButtonPressed = onFormButtonPressed)
    }
}