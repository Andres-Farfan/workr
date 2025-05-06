package com.example.workr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //RegistrationScreen()
            //ProfileEditScreen()
            //ProfileViewScreen()
            LoginScreen(
                onLoginClick = { /* acción al presionar Login */ },
                onRegisterClick = { /* acción al presionar Crear cuenta */ }
            )
            //PostulacionFormScreen()
            //JobDetailScreen()
        }
    }
}