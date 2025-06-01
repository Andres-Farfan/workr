package com.example.workr

import android.content.Intent
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
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Item listado de una vacante ofertada por una empresa en su lista propia.
 * @param userId Id de referencia pasado en la navegación.
 */
@Composable
fun CompanyVacancyItem(userId: String) {
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min).padding(vertical = 8.dp)
    ) {
        Icon(
            Icons.Rounded.Info,
            "Icono de vacante",
            modifier = Modifier.fillMaxHeight().aspectRatio(1.0f)
        )
        Column {
            Text("Nombre de la vacante")
            Text("Ubicación")
            Text("Publicado hace N días")
            Row (
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {}
                ) {
                    Text("Cerrar vacante")
                }
                Button(onClick = {
                    val intent = Intent(context, AspirantTrackingActivity::class.java)
                    intent.putExtra("user_id", userId)
                    context.startActivity(intent)
                }) {
                    Text("Ver postulados")
                }
            }
        }
    }
}