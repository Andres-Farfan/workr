package com.example.workr

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListing(
    userId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val jwt = sharedPreferences.getString(
        "jwt",
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjQ2YTlhMzA2LWM4NDctNGZiNS1hZWY2LTRlZjExNjQ1MjFiYSIsInR5cGUiOiJ1c2VyIiwiaWF0IjoxNzQ4ODA1MjU1LCJleHAiOjE3NDg4MDg4NTV9.KhD2Ag_J0yq87CApmGH7rpZ-5GLSvlxtn1cXXp9IJMM"
    ) ?: ""

    var state by remember { mutableStateOf<NotificationState>(NotificationState.Loading) }

    LaunchedEffect(Unit) {
        try {
            val data = getNotificationsData(jwt)
            state = NotificationState.Success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            state = NotificationState.Error("Error al obtener las notificaciones: ${e.message}")
        }
    }

    when (val currentState = state) {
        is NotificationState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(modifier),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF1565C0)
                )
            }
        }

        is NotificationState.Success -> {
            NotificationListScreen(
                notifications = currentState.data,
                modifier = modifier
            )
        }

        is NotificationState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(modifier),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentState.message,
                    color = Color(0xFFB00020),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

sealed class NotificationState {
    object Loading : NotificationState()
    data class Success(val data: Notifications) : NotificationState()
    data class Error(val message: String) : NotificationState()
}

@Composable
fun NotificationItem(
    title: String,
    description: String,
    date: String,
    isRead: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isRead) Color.White else Color(0xFFF1F8FF) // Soft blue for unread
    val titleColor = Color(0xFF0D47A1) // Consistent title color
    val descriptionColor = if (isRead) Color(0xFF37474F) else Color(0xFF1A237E) // Softer for read
    val fontWeight = if (isRead) FontWeight.Medium else FontWeight.SemiBold

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Icono Notificación",
                tint = if (isRead) Color(0xFF78909C) else Color(0xFF1565C0),
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = fontWeight,
                    fontSize = 16.sp,
                    color = titleColor,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = descriptionColor,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = Color(0xFF78909C),
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    notifications: Notifications,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = Color.White,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 16.dp,
                start = 8.dp,
                end = 8.dp
            )
        ) {
            items(notifications.notifications) { notification ->
                NotificationItem(
                    title = notification.title,
                    description = notification.description,
                    date = notification.creationDate,
                    isRead = notification.isRead
                )
            }
        }
    }
}

@Serializable
data class Notifications(val notifications: List<WorkRNotification>)

@Serializable
data class WorkRNotification(
    val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val isRead: Boolean,
    val creationDate: String
)

suspend fun getNotificationsData(jwt: String): Notifications {
    val endpoint = "/users/notifications"
    val method = HttpMethod.Get
    val headers = mapOf(HttpHeaders.Authorization to "Bearer $jwt")

    val response: HttpResponse = HTTPClientAPI.makeRequest(endpoint, method, body = null, headers)

    if (response.status == HttpStatusCode.Unauthorized) {
        throw Exception("Token vencido")
    }

    val responseBody = response.bodyAsText()
    val notificationList: List<WorkRNotification> = Gson().fromJson(responseBody, Array<WorkRNotification>::class.java).toList()

    return Notifications(notificationList)
}