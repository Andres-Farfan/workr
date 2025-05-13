package com.example.workr

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    LoginScreen(
                        onLoginClick = {
                            // Aquí puedes navegar a la pantalla principal después del login
                            // navController.navigate("home") // si existiera
                        },
                        onRegisterClick = {
                            navController.navigate("register")
                        }
                    )
                }
                composable("register") {
                    RegistrationScreen(
                        onBackClick = {
                            navController.popBackStack("login", inclusive = false)
                        }
                    )
                }
            }
        }
    }
}
