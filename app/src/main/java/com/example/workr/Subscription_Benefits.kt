package com.example.workr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.serialization.Serializable

class Subscription_Benefits : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Mock data for benefits
            val benefits = BenefitsInfo(
                statistics = BenefitDetail(
                    title = "Estadísticas Empresariales",
                    description = "Obtén análisis detallados sobre la eficiencia de tus vacantes y empleados, optimizando la productividad de tu oficina virtual y gestión de aspirantes.",
                    imageUrl = "https://res.cloudinary.com/dtdwsuiiy/image/upload/v1748395965/osooerztdxibfeu9qovs.jpg"
                ),
                googleMeet = BenefitDetail(
                    title = "Enlaces de Google Meet Automáticos",
                    description = "Genera automáticamente enlaces de Google Meet para entrevistas con aspirantes, agilizando el proceso de contratación.",
                    imageUrl = "https://res.cloudinary.com/dtdwsuiiy/image/upload/v1748395380/iodzdysh3ar2mcmxft2a.webp"
                )
            )

            SubscriptionBenefitsScreen(
                benefitsInfo = benefits,
                onBackClick = { finish() },
                onBackToPaymentClick = { finish() }
            )
        }
    }
}

@Serializable
data class BenefitsInfo(
    val statistics: BenefitDetail,
    val googleMeet: BenefitDetail
)

@Serializable
data class BenefitDetail(
    val title: String,
    val description: String,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionBenefitsScreen(
    benefitsInfo: BenefitsInfo,
    onBackClick: () -> Unit,
    onBackToPaymentClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beneficios de la Suscripción") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
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
                BenefitCard(
                    benefit = benefitsInfo.statistics,
                    icon = Icons.Default.AccountBox,
                    iconDescription = "Ícono de Estadísticas",
                )
                Spacer(modifier = Modifier.height(20.dp))
                BenefitCard(
                    benefit = benefitsInfo.googleMeet,
                    icon = Icons.Default.Call,
                    iconDescription = "Ícono de Videollamada",
                )
                Spacer(modifier = Modifier.height(20.dp))
                BackToPaymentHyperlink(onBackToPaymentClick)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun BenefitCard(
    benefit: BenefitDetail,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconDescription: String,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFE3F2FD)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = iconDescription,
                    tint = Color(0xFF1565C0),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )
                Text(
                    text = benefit.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = benefit.description,
                fontSize = 14.sp,
                color = Color(0xFF1A237E),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = benefit.imageUrl,
                contentDescription = "Captura de pantalla de ${benefit.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Composable
fun BackToPaymentHyperlink(onBackToPaymentClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onBackToPaymentClick)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Regresar a Pago",
                tint = Color(0xFF1565C0),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Volver a Información de Pago",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1565C0),
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }
    }
}