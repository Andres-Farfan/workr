package com.example.workr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun InterviewNotesScreen(navController: NavHostController) {
    var text by remember { mutableStateOf("") }

    Column (
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            text = "Notas de entrevista de aspirante",
            fontSize = 22.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        TextField(
            value = text,
            onValueChange = { newText: String -> text = newText },
            placeholder = { Text("Sin notas de entrevista") },
            singleLine = false,
            maxLines = 10,
            modifier = Modifier.fillMaxWidth().weight(1f)
        )
        Row (
            modifier = Modifier.weight(2f).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedButton (onClick = {
                navController.popBackStack()
            }) {
                Text("Regresar")
            }
            Button (onClick = {}) {
                Text("Guardar")
            }
        }
    }
}