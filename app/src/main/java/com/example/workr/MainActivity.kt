package com.example.workr

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtResponse: TextView = findViewById(R.id.txtResponse)

        // Se configuran el cliente http de Ktor y la url obtenida desde propiedades.
        val client = HttpClient(CIO)
        val url = BuildConfig.CONNECTION_TEST_URL

        // Creación de una corutina de kotlin para hacer una solicitud web (actividad asíncrona).
        runBlocking {
            launch {
                try {
                    // Con una solicitud correcta se mostrarán los datos de la respuesta en un TextView.
                    val response: HttpResponse = client.get(url)
                    txtResponse.text = "URL: $url\nHttp response status:\n${response.status}"
                }
                // Manejo de excepciones de conectividad.
                catch (e: Exception) {
                    e.printStackTrace()
                }
                finally {
                    // Liberación de recursos del ktor client.
                    client.close()
                }
            }
        }
    }
}