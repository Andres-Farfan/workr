package com.example.workr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun WorkRScaffold(
    navController: NavHostController,
    loginType: String,
    fromAspirantsTrackingList: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            if (!fromAspirantsTrackingList) {
                WorkRTopBar(navController = navController, loginType = loginType)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content(innerPadding)
        }
    }
}

@Composable
fun WorkRTopBar(
    navController: NavHostController,
    loginType: String,
    modifier: Modifier = Modifier
) {
    val isEmpleado = loginType == "user"
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = remember(loginType) {
        listOf(
            Triple(
                if (isEmpleado) "user_profile" else "company_profile",
                if (isEmpleado) R.drawable.ic_user else R.drawable.ic_company,
                "Perfil"
            ),
            Triple(
                if (isEmpleado) "company_listing" else "company_vacancies",
                if (isEmpleado) R.drawable.ic_jobs else R.drawable.ic_postulations,
                if (isEmpleado) "Empleos" else "Vacantes de empresa"
            ),
            Triple("virtual_office", R.drawable.ic_virtual_office, "Oficina Virtual"),
            Triple(
                if (isEmpleado) "notifications" else "company_info",
                if (isEmpleado) R.drawable.ic_notifications else R.drawable.folder,
                if (isEmpleado) "Notificaciones" else "Información"
            )
        )
    }

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
                if (currentRoute != route) {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = description,
                    tint = if (currentRoute == route)
                        colorResource(id = R.color.black)
                    else
                        colorResource(id = R.color.white),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}


