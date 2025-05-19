package com.example.workr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Componente usado para listar un aspirante en el Sistema Gestor de Aspirantes,
 * puede reusarse entre los 2 estados de aspirante inicial a contactado cambiando el
 * parámetro contacted.
 * @param name Nombre del aspirante listado.
 * @param onFormButtonPressed Callback usado cuando se presiona el botón de consulta de formulario.
 * @param onInterviewButtonPressed Callback usado cuando se presiona el botón de consulta de
 * notas de entrevista (si es que está visible por tratarse de un aspirante contactado).
 * @param contacted Booleano que indica si el aspirante a listar ya fue contactado
 * para una entrevista o no, permite alternar las opciones del item.
 */
@Composable
fun AspirantListItem(name: String, onFormButtonPressed: () -> Unit, onInterviewButtonPressed: (() -> Unit)? = null, contacted: Boolean = false) {
    Row (
        modifier = Modifier.height(IntrinsicSize.Min).padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Filled.AccountCircle,
            "Foto de perfil",
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1.0f)
        )
        Column (
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                name,
                fontSize = 20.sp,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = onFormButtonPressed) {
                    Text("Formulario")
                }
                if (contacted) {
                    OutlinedButton(onClick = {
                        if (onInterviewButtonPressed != null) {
                            onInterviewButtonPressed()
                        }
                    }) {
                        Text("Entrevista")
                    }
                }
            }
            if (contacted) {
                SelectionToggleButton(onToggle = {})
            }
        }
    }
}

/**
 * Componente de botón alternador de estados de selección,
 * se usa para indicar si un aspirante está seleccionado en la
 * lista de aspirantes contactados.
 * @param onToggle Callback que será invocado cuando el botón
 * alternador cambie de estado, recibirá el nuevo valor de
 * dicho estado.
 */
@Composable
fun SelectionToggleButton(onToggle: (selected: Boolean) -> Unit) {
    var selected by remember { mutableStateOf(false) }

    Button(
        onClick = {
            selected = !selected
            onToggle(selected)
        },
        colors = ButtonDefaults.buttonColors(containerColor = if (selected) Color.Gray else Color.Blue),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = if (selected) "Deseleccionar" else "Seleccionar")
    }
}