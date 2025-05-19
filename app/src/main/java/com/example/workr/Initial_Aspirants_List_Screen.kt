package com.example.workr

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Componente de lista de aspirantes iniciales en una vacante
 * en el Sistema Gestor de Aspirantes.
 */
@Composable
fun InitialAspirantsListScreen() {
    Column (
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AspirantListItem(name = "Nombre del aspirante")
        AspirantListItem(name = "Nombre del aspirante")
        AspirantListItem(name = "Nombre del aspirante")
        AspirantListItem(name = "Nombre del aspirante")
        AspirantListItem(name = "Nombre del aspirante")
        AspirantListItem(name = "Nombre del aspirante")
        AspirantListItem(name = "Nombre del aspirante")
        AspirantListItem(name = "Nombre del aspirante")
        AspirantListItem(name = "Nombre del aspirante")
        AspirantListItem(name = "Nombre del aspirante")
    }
}