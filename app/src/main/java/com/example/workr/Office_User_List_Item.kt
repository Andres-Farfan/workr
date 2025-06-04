package com.example.workr

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Componente que muestra los datos de un usuario en la oficina virtual como item para una lista.
 * @param user Datos del usuario a mostrar en el item de lista.
 */
@Composable
fun OfficeUserListItem(user: OfficeUser) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min).padding(8.dp)
    ) {
        Icon(
            Icons.Rounded.AccountCircle,
            "Icono de usuario",
            modifier = Modifier.fillMaxHeight().aspectRatio(1.0f)
        )
        Column (
            modifier = Modifier.weight(1.0f),
        ) {
            Text(user.name, fontSize = 24.sp, color = Color.Blue)
            Text(user.position, fontSize = 16.sp)
        }
        Canvas(modifier = Modifier.fillMaxHeight().aspectRatio(0.5f)) {
            drawCircle(color = Color.Red, radius = 24.0f)
        }
    }
}