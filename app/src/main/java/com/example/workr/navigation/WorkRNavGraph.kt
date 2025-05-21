package com.example.workr.navigation

import androidx.compose.runtime.Composable
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

@Composable
fun WorkRApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController, onRegisterClick = {navController.navigate("register")
                }
            )
        }

        composable("user_profile") {
            ProfileViewScreen(navController, isEmpleado = true)
        }

        composable("company_profile") {
            PerfilEmpresarialScreen(navController = navController, isEmpleado = false)
        }

        // Agrega aquí más pantallas según vayas creando
        composable("job_creation") {
            CreateJobScreen(navController, isEmpleado = false)
        }
        composable("postulation_form") {
            PostulacionFormScreen(navController, isEmpleado = true)
        }
        composable("initial_aspirant_postulation_form") {
            PostulacionFormScreen(navController, isEmpleado = false, fromAspirantsTrackingList = "initial")
        }
        composable("contacted_aspirant_postulation_form") {
            PostulacionFormScreen(navController, isEmpleado = false, fromAspirantsTrackingList = "contacted")
        }
        composable("company_listing") {
            CompanyListing(navController, isEmpleado = true)
        }
        composable("list_vacancies") {
            VacantesScreen(navController, isEmpleado = true)
        }
        composable("job_detail") {
            JobDetailScreen(navController, isEmpleado = true)
        }
        composable("profile_edit_user"){
            ProfileEditScreen(navController, isEmpleado = true)
        }
        composable("register_user"){
            RegistrationScreen(navController, isEmpleado = true)
        }
        composable("complete_profile_company"){
            CompletePerfile(navController, isEmpleado = false)
        }
        composable("company_register"){
            BusinessCreationScreen(navController, isEmpleado = false)
        }
        composable("notifications"){
            NotificationsScreen(navController, isEmpleado = true)
        }
        composable("aspirant_tracking_system") {
            AspirantTrackingScreen(navController)
        }
        composable("interview_notes") {
            InterviewNotesScreen(navController)
        }
    }
}