package com.example.workr

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AspirantListItem(
    name: String,
    onFormButtonPressed: () -> Unit,
    onInterviewButtonPressed: (() -> Unit)? = null,
    contacted: Boolean = false
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = "Foto de perfil de $name",
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1.0f)
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = name,
                fontSize = 20.sp,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onFormButtonPressed,
                    border = BorderStroke(1.dp, colorResource(id = com.example.workr.R.color.blue_WorkR)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(id = com.example.workr.R.color.blue_WorkR)
                    )
                ) {
                    Text("Formulario")
                }
                if (contacted && onInterviewButtonPressed != null) {
                    OutlinedButton(
                        onClick = onInterviewButtonPressed,
                        border = BorderStroke(1.dp, colorResource(id = com.example.workr.R.color.blue_WorkR)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colorResource(id = com.example.workr.R.color.blue_WorkR)
                        )
                    ) {
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

@Composable
fun SelectionToggleButton(onToggle: (selected: Boolean) -> Unit) {
    var selected by remember { mutableStateOf(false) }

    Button(
        onClick = {
            selected = !selected
            onToggle(selected)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color.Gray else colorResource(id = com.example.workr.R.color.blue_WorkR)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = if (selected) "Deseleccionar" else "Seleccionar")
    }
}
