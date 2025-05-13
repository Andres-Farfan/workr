package com.example.workr

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun WorkRTopBar(
    navController: NavHostController, isEmpleado: Boolean, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("workr_prefs", Context.MODE_PRIVATE)
    val hasCompany = remember { mutableStateOf(sharedPref.getBoolean("has_company", false)) }

    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Work-R") },
        backgroundColor = colorResource(id = R.color.blue_WorkR),
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
                            navController.navigate("register_company")
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
                    }
                }
            }
        }
    )
}
