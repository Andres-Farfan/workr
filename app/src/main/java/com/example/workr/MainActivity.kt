package com.example.workr

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var txtResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtResponse = findViewById(R.id.txtResponse)
        txtResponse.text = "Loading request..."

        // Se lanza una corutina con la tarea de hacer la solicitud web
        // usando un Scope de corutinas no-bloqueante al nivel del ciclo de vida de la Activity.
        lifecycleScope.launch { makeRequest() }
    }

    /**
     * Hace una solicitud HTTP y muestra el resultado con texto en la vista.
     */
    suspend fun makeRequest() {
        // Configuración del cliente http y la URL obtenida desde propiedades.
        val client = HttpClient(CIO)
        val url = BuildConfig.CONNECTION_TEST_URL

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
            println("Ktor client closed")
        }
    }
}