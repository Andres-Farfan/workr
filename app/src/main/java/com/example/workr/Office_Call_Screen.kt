package com.example.workr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.sharp.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Ventana utilizada para una llamada grupal de oficina.
 * @param inCallUsers Estado mutable con la lista de usuarios actualmente en la llamada.
 * @param onMicToggle Callback que será invocado cuando el usuario alterne el estado del micrófono.
 * @param onExitPressed Callback que será invocado cuando el usuario presione el botón para salir de llamada.
 */
@Composable
fun OfficeCallScreen(inCallUsers: List<OfficeUser>, onMicToggle: (isMicOn: Boolean) -> Unit, onExitPressed: () -> Unit) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Reunión grupal",
            fontSize = 32.sp,
            modifier = Modifier.padding(8.dp)
        )
        Icon(
            Icons.Sharp.Call,
            "Icono de llamada",
            modifier = Modifier.weight(1.0f).fillMaxSize()
        )
        Text(
            "Conectados",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp)
        )
        LazyColumn (
            modifier = Modifier.weight(2.0f)
        ) {
            items(inCallUsers) { user ->
                OfficeUserListItem(user)
            }
        }
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth().padding(32.dp)
        ) {
            OutlinedButton(onClick = onExitPressed) {
                Text("Salir", fontSize = 24.sp)
            }
            ToggleMicButton(onToggle = onMicToggle)
        }
    }
}

/**
 * Componente auxiliar de botón de micrófono alternable entre estados.
 * @param onToggle Callback que será invocado cuando el botón cambie de estado.
 */
@Composable
fun ToggleMicButton(onToggle: (isMicOn: Boolean) -> Unit) {
    var isMicOn by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            isMicOn = !isMicOn
            onToggle(isMicOn)
        },
        modifier = Modifier.background(
            color = if (isMicOn) Color.Blue else Color.Gray,
            shape = CircleShape
        )
    ) {
        Icon(
            if (isMicOn) Icons.Rounded.Mic else Icons.Rounded.MicOff,
            "Icono de micrófono",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}