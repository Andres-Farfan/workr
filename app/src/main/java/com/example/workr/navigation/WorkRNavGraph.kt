package com.example.workr.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.workr.AspirantTrackingScreen
import com.example.workr.BusinessCreationScreen
import com.example.workr.CompanyListing
import com.example.workr.CompletePerfile
import com.example.workr.CreateJobScreen
import com.example.workr.InterviewNotesScreen
import com.example.workr.JobDetailScreen
import com.example.workr.LoginScreen
import com.example.workr.NotificationsScreen
import com.example.workr.ProfileViewScreen
import com.example.workr.PerfilEmpresarialScreen
import com.example.workr.PostulacionFormScreen
import com.example.workr.ProfileEditScreen
import com.example.workr.RegistrationScreen
import com.example.workr.VacantesScreen
import com.example.workr.VirtualOfficeScreen

@Composable
fun WorkRApp() {
    val navController = rememberNavController()

    // Estados globales para tipo de usuario e ID
    val loginType = rememberSaveable { mutableStateOf("") }
    val userId = rememberSaveable { mutableStateOf("") }
//Cambiar starDestination por login una vez finalizadas las pruebas*********
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { type, id ->
                    loginType.value = type
                    userId.value = id
                    navController.navigate(if (type == "employee") "user_profile" else "company_profile")
                },
                onRegisterClick = {
                    navController.navigate("register_user")
                }
            )
        }

        composable("user_profile") {
            ProfileViewScreen(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value
            )
        }

        composable("company_profile") {
            PerfilEmpresarialScreen(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value
            )
        }

        composable("job_creation") {
            CreateJobScreen(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value
            )
        }

        composable("postulation_form") {
            PostulacionFormScreen(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value
            )
        }

        composable("initial_aspirant_postulation_form") {
            PostulacionFormScreen(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value,
                fromAspirantsTrackingList = "initial"
            )
        }

        composable("contacted_aspirant_postulation_form") {
            PostulacionFormScreen(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value,
                fromAspirantsTrackingList = "contacted"
            )
        }

        composable("company_listing") {
            CompanyListing(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value
            )
        }

        composable("list_vacancies") {
            VacantesScreen(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value
            )
        }

        composable("job_detail") {
            JobDetailScreen(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value
            )
        }

        composable("profile_edit_user") {
            ProfileEditScreen(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value
            )
        }

        // ✅ Ya no se pasa loginType ni userId
        composable("register_user") {
            RegistrationScreen(
                navController = navController
            )
        }

        composable("complete_profile_company") {
            CompletePerfile(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value
            )
        }

        // ✅ Ya no se pasa loginType ni userId
        composable("company_register") {
            BusinessCreationScreen(
                navController = navController
            )
        }

        composable("notifications") {
            NotificationsScreen(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value
            )
        }

        composable("virtual_office") {
            VirtualOfficeScreen(
                navController = navController,
                loginType = loginType.value,
                userId = userId.value
            )
        }

        composable("aspirant_tracking_system") {
            AspirantTrackingScreen(
                globalNavController = navController,
                loginType = loginType.value,
                userId = userId.value
            )
        }

        composable("interview_notes") {
            InterviewNotesScreen(
                navController = navController
            )
        }
    }
}


