package com.example.workr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class Company_Payment : ComponentActivity() {
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var paymentIntentClientSecret: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Stripe
        PaymentConfiguration.init(applicationContext, BuildConfig.STRIPE_PUBLISHABLE_KEY)

        paymentSheet = PaymentSheet(this) { paymentSheetResult ->
            onPaymentResult(paymentSheetResult)
        }

        setContent {
            val context = LocalContext.current
            val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val jwt = sharedPreferences.getString(
                "jwt",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjQ2YTlhMzA2LWM4NDctNGZiNS1hZWY2LTRlZjExNjQ1MjFiYSIsInR5cGUiOiJ1c2VyIiwiaWF0IjoxNzQ4ODA1MjU1LCJleHAiOjE3NDg4MDg4NTV9.KhD2Ag_J0yq87CApmGH7rpZ-5GLSvlxtn1cXXp9IJMM"
            ) ?: ""

            CompanyPaymentScreen(
                jwt = jwt,
                onBackClick = { finish() },
                onViewBenefitsClick = {
                    startActivity(Intent(this, Subscription_Benefits::class.java))
                },
                onPaymentInitiated = { clientSecret ->
                    paymentIntentClientSecret = clientSecret
                    presentPaymentSheet()
                }
            )
        }
    }

    private fun presentPaymentSheet() {
        val configuration = PaymentSheet.Configuration(
            merchantDisplayName = "WorkR",
            allowsDelayedPaymentMethods = false
        )
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration)
    }

    private fun onPaymentResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                setContent {
                    PaymentResultScreen(
                        isSuccess = true,
                        onAnimationComplete = { finish() }
                    )
                }
            }
            is PaymentSheetResult.Failed -> {
                setContent {
                    PaymentResultScreen(
                        isSuccess = false,
                        errorMessage = paymentSheetResult.error.message,
                        onAnimationComplete = { finish() }
                    )
                }
            }
            is PaymentSheetResult.Canceled -> {
                setContent {
                    PaymentResultScreen(
                        isSuccess = false,
                        errorMessage = "Pago cancelado",
                        onAnimationComplete = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentResultScreen(
    isSuccess: Boolean,
    errorMessage: String? = null,
    onAnimationComplete: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var scale by remember { mutableFloatStateOf(0.5f) }
    var opacity by remember { mutableFloatStateOf(0f) }

    // Animation for the icon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    LaunchedEffect(Unit) {
        animate(
            initialValue = 0.5f,
            targetValue = 1f,
            animationSpec = spring(dampingRatio = 0.6f)
        ) { value, _ -> scale = value }

        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(500)
        ) { value, _ -> opacity = value }

        delay(3000)
        onAnimationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSuccess) Color(0xFFE8F5E9) else Color(0xFFFFEBEE))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .scale(scale)
                .alpha(opacity),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated icon
            Image(
                painter = rememberAsyncImagePainter(
                    if (isSuccess) R.drawable.ic_payment_success else R.drawable.ic_payment_failed
                ),
                contentDescription = if (isSuccess) "Success" else "Failed",
                modifier = Modifier
                    .size(120.dp)
                    .scale(pulseScale)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (isSuccess) "¡Pago exitoso!" else "Pago no completado",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSuccess) Color(0xFF2E7D32) else Color(0xFFC62828)
            )

            if (!isSuccess && errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    fontSize = 16.sp,
                    color = Color(0xFFC62828)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Redirigiendo al menú principal...",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Serializable
data class CompanyInfo(
    val name: String,
    val type: String,
    val address: String,
    val description: String,
    val mission: String,
    val vision: String,
    val profilePicture: String,
    val commercialSector: String,
    val employeeCount: Int,
    val contactLinks: List<ContactLink>,
    val companyPayInfo: CompanyPayInfo
)

@Serializable
data class ContactLink(
    val platform: String,
    val link: String
)

@Serializable
data class CompanyPayInfo(
    val employeesCount: Int,
    val pricePerUser: Int
)

@Serializable
data class PaymentIntentResponse(
    val clientSecret: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyPaymentScreen(
    jwt: String,
    onBackClick: () -> Unit,
    onViewBenefitsClick: () -> Unit,
    onPaymentInitiated: (String) -> Unit,
    paymentStatus: String? = null
) {
    val scope = rememberCoroutineScope()
    var companyInfo by remember { mutableStateOf<CompanyInfo?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var dialogClientSecret by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch company payment info
    LaunchedEffect(Unit) {
        try {
            isLoading = true
            companyInfo = getCompanyPayInfo(jwt)
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }

    if (showPaymentDialog && dialogClientSecret.isNotEmpty()) {
        PaymentDialog(
            onDismiss = { showPaymentDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pago de mensualidad") },
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
                Spacer(modifier = Modifier.height(20.dp))
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (errorMessage != null) {
                    Text(
                        text = "Error: $errorMessage",
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    companyInfo?.let { info ->
                        CompanyInfoCard(info)
                        Spacer(modifier = Modifier.height(20.dp))
                        MonthlySummaryCard(info.companyPayInfo)
                        Spacer(modifier = Modifier.height(20.dp))
                        StripePaymentButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        isLoading = true
                                        val response = createPaymentIntent(jwt)
                                        dialogClientSecret = response.clientSecret
                                        showPaymentDialog = true
                                        onPaymentInitiated(response.clientSecret)
                                    } catch (e: Exception) {
                                        errorMessage = e.message
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            enabled = !isLoading
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        BenefitsHyperlink(onViewBenefitsClick)
                    } ?: run {
                        Text(
                            text = "Loading payment info...",
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

suspend fun getCompanyPayInfo(jwt: String): CompanyInfo {
    val endpoint = "companies/company_pay_info"
    val method = HttpMethod.Get
    val headers = mapOf(HttpHeaders.Authorization to "Bearer $jwt")

    val response = HTTPClientAPI.makeRequest(endpoint, method, body = null, headers)

    if (response.status == HttpStatusCode.Unauthorized) {
        throw Exception("Expired token")
    }

    if (!response.status.isSuccess()) {
        throw Exception("Failed to fetch company info: ${response.status}")
    }

    val responseBody = response.bodyAsText()
    return try {
        Gson().fromJson(responseBody, CompanyInfo::class.java)
            ?: throw Exception("Failed to parse company info: Empty response")
    } catch (e: Exception) {
        throw Exception("Failed to parse company info: ${e.message}")
    }
}

suspend fun createPaymentIntent(jwt: String): PaymentIntentResponse {
    try {
        val endpoint = "companies/create_payment_intent"
        val method = HttpMethod.Post
        val headers = mapOf(
            HttpHeaders.Authorization to "Bearer $jwt",
            HttpHeaders.ContentType to ContentType.Application.Json.toString()
        )

        val response = HTTPClientAPI.makeRequest(endpoint, method, body = null, headers)

        when {
            response.status == HttpStatusCode.Unauthorized ->
                throw Exception("Token expirado o no válido")

            response.status == HttpStatusCode.PermanentRedirect ->
                throw Exception("Error de configuración: Redirección permanente detectada. Verifica la URL del endpoint.")

            !response.status.isSuccess() ->
                throw Exception("Error del servidor: ${response.status}")
        }

        val responseBody = response.bodyAsText()
        return Gson().fromJson(responseBody, PaymentIntentResponse::class.java)
            ?: throw Exception("Respuesta del servidor no válida")
    } catch (e: Exception) {
        throw Exception("Error al crear intención de pago: ${e.message}")
    }
}

@Composable
fun CompanyInfoCard(companyInfo: CompanyInfo) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFFFFF), Color(0xFFE3F2FD))
                )
            )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = companyInfo.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            InfoRowDisplay(label = "Nombre:", value = companyInfo.name)
            InfoRowDisplay(label = "Tipo:", value = companyInfo.type)
            InfoRowDisplay(label = "Sector:", value = companyInfo.commercialSector)
            InfoRowDisplay(label = "Empleados:", value = companyInfo.employeeCount.toString())
            InfoRowDisplay(label = "Dirección:", value = companyInfo.address)
            Spacer(modifier = Modifier.height(12.dp))
            InfoRowDisplay(label = "Descripción:", value = companyInfo.description, isBody = true)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRowDisplay(label = "Misión:", value = companyInfo.mission, isBody = true)
            InfoRowDisplay(label = "Visión:", value = companyInfo.vision, isBody = true)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Enlaces de contacto:",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0D47A1),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            companyInfo.contactLinks.forEach { link ->
                Text(
                    text = "${link.platform}: ${link.link}",
                    fontSize = 14.sp,
                    color = Color(0xFF1A237E),
                    modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun InfoRowDisplay(label: String, value: String, isBody: Boolean = false) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontSize = if (isBody) 14.sp else 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF0D47A1),
            modifier = Modifier.weight(0.3f)
        )
        Text(
            text = value,
            fontSize = if (isBody) 14.sp else 16.sp,
            color = Color(0xFF1A237E),
            modifier = Modifier.weight(0.7f)
        )
    }
}

@Composable
fun MonthlySummaryCard(payInfo: CompanyPayInfo) {
    val totalCost = payInfo.employeesCount * payInfo.pricePerUser

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Resumen de la suscripción",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            InfoRowDisplay(label = "Monto total:", value = "$$totalCost")
            InfoRowDisplay(label = "Empleados cubiertos:", value = payInfo.employeesCount.toString())
            InfoRowDisplay(label = "Costo por empleado:", value = "$${payInfo.pricePerUser}")
        }
    }
}

@Composable
fun StripePaymentButton(onClick: () -> Unit, enabled: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (enabled) Color(0xFF635BFF) else Color(0xFFC6C4FF),
        shadowElevation = 2.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_company),
                        contentDescription = "Stripe",
                        tint = Color(0xFF635BFF),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "PAGAR AHORA",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun BenefitsHyperlink(onViewBenefitsClick: () -> Unit) {
    Text(
        text = "Ver beneficios de suscripción",
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF1565C0),
        style = TextStyle(textDecoration = TextDecoration.Underline),
        modifier = Modifier
            .clickable(onClick = onViewBenefitsClick)
            .padding(8.dp)
    )
}