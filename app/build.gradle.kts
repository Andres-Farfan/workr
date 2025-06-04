import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.workr"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.workr"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Se lee un archivo de propiedades para usarlas en este archivo de Build.
        // Nota: El archivo debe ser correctamente ignorado del repositorio con .gitignore.
        val propertiesFile = rootProject.file("config.properties")
        val properties = Properties()
        properties.load(FileInputStream(propertiesFile))

        // Se hacen accesibles para el código las propiedades de configuración con BuildConfig.
        buildConfigField("String", "BACKEND_BASE_URL", properties.getProperty("BACKEND_BASE_URL"))
        buildConfigField("String", "BACKEND_API_KEY", properties.getProperty("BACKEND_API_KEY"))
        buildConfigField("String", "CALLING_APP_ID", properties.getProperty("CALLING_APP_ID"))
    }

    buildFeatures {
        // Se habilita la generación del objeto BuildConfig para el proyecto.
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}
val ktor_version: String by project
val nav_version: String by project
val agora_video_sdk_version: String by project
dependencies {
    implementation("io.agora.rtc:full-sdk:${agora_video_sdk_version}")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson:$ktor_version")
    implementation("androidx.navigation:navigation-compose:2.7.7") // Usa la última versión si es diferente
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("androidx.compose.ui:ui:1.4.0") // Base de Jetpack Compose
    implementation ("androidx.compose.material:material:1.4.0") // Componentes Material Design
    implementation ("androidx.compose.material:material-icons-extended:1.4.0") // Iconos Material Design
    implementation ("androidx.compose.ui:ui-tooling-preview:1.4.0") // Herramientas de vista previa
    implementation ("androidx.activity:activity-compose:1.7.0") // Integración con Activity
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("io.coil-kt:coil-svg:2.2.2")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}