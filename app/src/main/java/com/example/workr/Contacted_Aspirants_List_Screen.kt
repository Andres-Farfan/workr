package com.example.workr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

/**
 * Sub-ventana de lista de aspirantes contactados para una vacante
 * en el Sistema Gestor de Aspirantes.
 * @param onFormButtonPressed Callback usado cuando se presiona el botón de consulta de formulario.
 * @param onInterviewButtonPressed Callback usado cuando se presiona el botón de consulta de
 * notas de entrevista.
 */
@Composable
fun ContactedAspirantsListScreen(onFormButtonPressed: () -> Unit, onInterviewButtonPressed: () -> Unit) {
    Column (
        modifier = Modifier.fillMaxHeight()
    ) {
        Column (
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            AspirantListItem("Nombre del aspirante", onFormButtonPressed, onInterviewButtonPressed, contacted = true)
            AspirantListItem("Nombre del aspirante", onFormButtonPressed, onInterviewButtonPressed, contacted = true)
            AspirantListItem("Nombre del aspirante", onFormButtonPressed, onInterviewButtonPressed, contacted = true)
            AspirantListItem("Nombre del aspirante", onFormButtonPressed, onInterviewButtonPressed, contacted = true)
            AspirantListItem("Nombre del aspirante", onFormButtonPressed, onInterviewButtonPressed, contacted = true)
        }
        Box (
            modifier = Modifier.background(color = colorResource(id = R.color.gray_WorkR)).padding(vertical = 8.dp)
        ) {
            Button(
                onClick = {},
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.cian_WorkR),
                    contentColor = colorResource(id = R.color.black)
                )
            ) {
                Text("Proceder con contratación de seleccionados")
            }
        }
    }
}