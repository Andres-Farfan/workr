package com.example.workr

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*
import com.example.workr.ui.theme.WorkRTheme
import com.google.gson.Gson
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

class EmployeesCharts : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkRTheme {
                EmployeeChartsContent()
            }
        }
    }
}

@Composable
fun EmployeeChartsContent() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val jwt = sharedPreferences.getString(
        "jwt",
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImVkYmJjZTliLTBkMzgtNDAzZi1iOTFjLWNmMDU1ZjUyYWU1NiIsInR5cGUiOiJjb21wYW55IiwiaWF0IjoxNzQ4Mzk1NTYwLCJleHAiOjE3NDgzOTkxNjB9.AqzWGtNMPdhLGwUWxbWdJ2_lEpQ5KC_2d-zYy_gt0yw"
    )

    var state by remember { mutableStateOf<EmployeeChartState>(EmployeeChartState.Loading) }

    LaunchedEffect(Unit) {
        try {
            val data = getEmployeeChartsData(jwt!!)
            state = EmployeeChartState.Success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            state = EmployeeChartState.Error("Error loading employee charts data")
        }
    }

    when (val currentState = state) {
        is EmployeeChartState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is EmployeeChartState.Success -> {
            EmployeeChartsScreen(
                data = currentState.data,
                onBackClick = { /* Handle back click */ }
            )
        }

        is EmployeeChartState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = currentState.message, color = Color.Red)
            }
        }
    }
}

sealed class EmployeeChartState {
    object Loading : EmployeeChartState()
    data class Success(val data: EmployeeChartsResponse) : EmployeeChartState()
    data class Error(val message: String) : EmployeeChartState()
}

@Serializable
data class EmployeeChartData(
    val points: List<Point>,
    val labels: List<String>
)

@Serializable
data class TopWorker(
    val id: String,
    val full_name: String,
    val profile_picture: String,
    val hours_worked: String,
    val start_date: String
)

@Serializable
data class EmployeeChartsResponse(
    val workTime: EmployeeChartData,
    val topWorkers: List<TopWorker>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeChartsScreen(data: EmployeeChartsResponse, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas de empleados") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5FAFC)
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                EmployeeLineChartScreen(
                    data = data.workTime.points,
                    labels = data.workTime.labels,
                    title = "Horas trabajadas por mes",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Top Workers Section
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Empleados con más horas trabajadas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        data.topWorkers.forEach { worker ->
                            WorkerItem(worker)
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmployeeLineChartScreen(
    data: List<Point>,
    labels: List<String>,
    title: String,
    modifier: Modifier = Modifier
) {
    val stepsX = data.size - 1
    val stepSizeX = 30
    val primaryColor = Color(0xFF1565C0)

    val maxY = (data.maxOfOrNull { it.y } ?: 1f)
    val stepValueY = when {
        maxY <= 5f -> 1f
        else -> {
            val roughStep = maxY / 5f
            when {
                roughStep <= 1f -> 1f
                roughStep <= 2f -> 2f
                roughStep <= 5f -> 5f
                roughStep <= 10f -> 10f
                else -> kotlin.math.ceil(roughStep / 10f) * 10f
            }
        }
    }
    val stepsY = kotlin.math.ceil(maxY / stepValueY).toInt()

    val xAxisData = AxisData.Builder()
        .axisStepSize(stepSizeX.dp)
        .backgroundColor(Color.Transparent)
        .steps(stepsX)
        .labelData { i -> if (i < labels.size && i % 3 == 0) labels[i] else "" }
        .labelAndAxisLinePadding(12.dp)
        .axisLineColor(Color(0xFF0D47A1))
        .axisLabelColor(Color(0xFF0D47A1))
        .axisLabelFontSize(12.sp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(stepsY)
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(12.dp)
        .labelData { i -> (i * stepValueY).roundToInt().toString() }
        .axisLineColor(Color(0xFF0D47A1))
        .axisLabelColor(Color(0xFF0D47A1))
        .axisLabelFontSize(12.sp)
        .build()

    val chartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = data,
                    lineStyle = LineStyle(
                        color = primaryColor,
                        lineType = LineType.SmoothCurve(isDotted = false),
                        width = 4f
                    ),
                    intersectionPoint = IntersectionPoint(
                        color = primaryColor,
                        radius = 6.dp
                    ),
                    selectionHighlightPoint = SelectionHighlightPoint(
                        color = Color(0xFFE3F2FD)
                    ),
                    shadowUnderLine = ShadowUnderLine(
                        color = primaryColor.copy(alpha = 0.2f)
                    ),
                    selectionHighlightPopUp = SelectionHighlightPopUp(
                        backgroundColor = Color.White,
                        backgroundAlpha = 0.9f,
                        labelColor = Color(0xFF0D47A1),
                        labelSize = 12.sp
                    )
                )
            )
        ),
        backgroundColor = Color.Transparent,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(
            color = Color(0xFFE3F2FD),
            lineWidth = 1.dp
        )
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                lineChartData = chartData
            )
        }
    }
}

@Composable
fun WorkerItem(worker: TopWorker) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = worker.full_name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "Horas trabajadas: ${worker.hours_worked}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Text(
            text = "${worker.hours_worked}h",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF1565C0)
        )
    }
}

suspend fun getEmployeeChartsData(jwt: String): EmployeeChartsResponse {
    val endpoint = "/companies/employees_charts"
    val method = HttpMethod.Get
    val headers = mapOf(HttpHeaders.Authorization to "Bearer $jwt")

    val response: HttpResponse = HTTPClientAPI.makeRequest(endpoint, method, body = null, headers)

    if (response.status == HttpStatusCode.Unauthorized) {
        throw Error("Token expired")
    }

    val responseBody = response.bodyAsText()
    return Gson().fromJson(responseBody, EmployeeChartsResponse::class.java)
}