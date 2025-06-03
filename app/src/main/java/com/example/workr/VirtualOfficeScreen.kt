package com.example.workr

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun VirtualOfficeScreen(
    navController: NavHostController,
    loginType: String,
    userId: String
) {
    val context = LocalContext.current
    val isEmpleado = loginType == "user"

    WorkRScaffold(
        navController = navController,
        loginType = loginType,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isEmpleado)
                    "Bienvenido a tu Oficina Virtual"
                else
                    "Accede a la Oficina Virtual de tu empresa"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                val intent = Intent()
                intent.setClassName("com.workr.office", "com.godot.game.GodotApp")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    print("Error al iniciar oficina virtual: ${e.message}")
                    Toast.makeText(context, "Error al abrir oficina virtual", Toast.LENGTH_LONG).show()
                }
            }) {
                Text("Entrar a la Oficina Virtual")
            }
        }
    }
}