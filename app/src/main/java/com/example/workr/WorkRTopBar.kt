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
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            WorkRTopBar(navController = navController, loginType = loginType)
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
                if (isEmpleado) "company_listing" else "aspirant_tracking_system",
                if (isEmpleado) R.drawable.ic_jobs else R.drawable.ic_postulations,
                if (isEmpleado) "Empleos" else "Gestión de Aspirantes"
            ),
            Triple("virtual_office", R.drawable.ic_virtual_office, "Oficina Virtual"),
            Triple("notifications", R.drawable.ic_notifications, "Notificaciones")
        )
    }

    TopAppBar(
        title = { Text("Work-R",color = colorResource(id = R.color.white)) },

        backgroundColor = colorResource(id = R.color.blue_WorkR),
        modifier = Modifier.height(70.dp),
        actions = {
            Box(Modifier.wrapContentSize(Alignment.TopEnd)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground), // icono default
                        contentDescription = "Menú",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    if (isEmpleado) {
                        DropdownMenuItem(onClick = {
                            navController.navigate("user_profile")
                            expanded = false
                        }) {
                            Text("Perfil")
                        }

                        DropdownMenuItem(onClick = {
                            navController.navigate("postulation_form")
                            expanded = false
                        }) {
                            Text("Formulario de postulación")
                        }
                    } else {
                        DropdownMenuItem(onClick = {
                            navController.navigate("company_register")
                            expanded = false
                        }) {
                            Text("Registrar empresa")
                        }

                        if (hasCompany.value) {
                            DropdownMenuItem(onClick = {
                                navController.navigate("company_profile")
                                expanded = false
                            }) {
                                Text("Ver perfil de empresa")
                            }
                        }

                        DropdownMenuItem(onClick = {
                            navController.navigate("job_creation")
                            expanded = false
                        }) {
                            Text("Publicar vacante")
                        }
                    }

                    Divider()

                    DropdownMenuItem(onClick = {
                        // Limpiar preferencias al cerrar sesión
                        with(sharedPref.edit()) {
                            clear()
                            apply()
                        }
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                        expanded = false
                    }) {
                        Text("Cerrar sesión")
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


