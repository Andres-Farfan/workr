package com.example.workr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

// Versión simple: solo para las pantallas normales
@Composable
fun WorkRTopBar(
    navController: NavHostController,
    isEmpleado: Boolean,
    loginType: String,
    userId: String,
    modifier: Modifier = Modifier
) {
    val isEmpleado = loginType == "employee"
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val items = if (isEmpleado) {
        listOf(
            Triple("user_profile", R.drawable.ic_user, "Perfil"),
            Triple("company_listing", R.drawable.ic_jobs, "Empleos")
        )
    } else {
        listOf(
            Triple("company_profile", R.drawable.ic_company, "Perfil empresa"),
            Triple("aspirant_tracking_system", R.drawable.ic_postulations, "Gestion de Aspirantes")
        )
    } + listOf(
        Triple("virtual_office", R.drawable.ic_virtual_office, "Oficina Virtual"),
        Triple("notifications", R.drawable.ic_notifications, "Notificaciones")
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(colorResource(id = R.color.blue_WorkR))
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { (route, iconId, description) ->
            IconButton(onClick = {
                navController.navigate(route)
            }) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = description,
                    tint = if (currentRoute == route) colorResource(id = R.color.black) else colorResource(id = R.color.white),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}