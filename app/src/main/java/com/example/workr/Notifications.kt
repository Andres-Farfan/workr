package com.example.workr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun NotificationsScreen(
    navController: NavHostController,
    loginType: String,
    userId: String
) {
    WorkRScaffold(
        navController = navController,
        loginType = loginType,
    ) { innerPadding ->
        NotificationListing(
            userId = userId,
            modifier = Modifier.padding(innerPadding),
            onBackClick = { navController.popBackStack() }
        )
    }
}


