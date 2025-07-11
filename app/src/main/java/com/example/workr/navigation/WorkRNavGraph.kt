package com.example.workr.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.workr.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {
    private val _loginType = MutableStateFlow("")
    val loginType: StateFlow<String> = _loginType

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _companyId = MutableStateFlow("")
    val companyId: StateFlow<String> = _companyId

    fun updateLogin(type: String, id: String, companyId: String) {
        _loginType.value = type
        _userId.value = id
        _companyId.value = companyId
    }
}

@Composable
fun WorkRApp(loginViewModel: LoginViewModel = viewModel()) {
    val navController = rememberNavController()

    // ✅ Recolectamos el estado observable para que Compose escuche cambios
    val loginType by loginViewModel.loginType.collectAsState()
    val userId by loginViewModel.userId.collectAsState()
    val companyId by loginViewModel.companyId.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = Modifier.fillMaxSize()
    ) {
        // Pantallas SIN barra superior
        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { type, id, companyId ->
                    loginViewModel.updateLogin(type, id, companyId)
                    navController.navigate(
                        if (type == "user") "user_profile" else "company_profile"
                    ) {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("register_user") {
            RegistrationScreen(
                navController = navController)
        }

        composable("company_register") {
            BusinessCreationScreen(navController = navController)
        }

        // Pantallas CON barra superior (usan WorkRScaffold internamente)
        composable("user_profile") {
            ProfileViewScreen(
                navController = navController,
                loginType = loginType,
                userId = userId
            )
        }

        composable("company_profile") {
            PerfilEmpresarialScreen(
                navController = navController,
                loginType = loginType,
                userId = userId
            )
        }

        composable("job_creation") {
            CreateJobScreen(
                navController = navController,
                loginType = loginType,
                userId = userId
            )
        }

        composable("postulation_form") {
            PostulacionFormScreen(
                navController = navController,
                loginType = loginType,
                userId = userId
            )
        }

        composable("company_listing") {
            CompanyListing(
                navController = navController,
                loginType = loginType,
                userId = userId
            )
        }

        composable("list_vacancies") {
            VacantesScreen(
                navController = navController,
                loginType = loginType,
                userId = userId
            )
        }

        composable("job_detail") {
            JobDetailScreen(
                navController = navController,
                loginType = loginType,
                userId = userId
            )
        }

        composable("profile_edit_user") {
            ProfileEditScreen(
                navController = navController,
                loginType = loginType,
                userId = userId
            )
        }

        composable("complete_profile_company") {
            CompletePerfile(
                navController = navController,
                loginType = loginType,
                userId = userId
            )
        }

        composable("notifications") {
            NotificationsScreen(
                navController = navController,
                loginType = loginType,
                userId = userId
            )
        }

        composable("virtual_office") {
            VirtualOfficeScreen(
                navController = navController,
                loginType = loginType,
                userId = userId,
                companyId = companyId
            )
        }

        composable("company_vacancies") {
            CompanyVacanciesScreen(
                globalNavController = navController,
                loginType = loginType,
                userId = userId
            )
        }

        composable("company_info") {
            CompanyInfoScreen()
        }
    }
}



