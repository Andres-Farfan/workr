package com.example.workr

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
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
        lifecycleScope.launch { doRequest() }
    }

    /**
     * Hace una solicitud HTTP y muestra el resultado con texto en la vista.
     */
    suspend fun doRequest() {
        // Configuración del cliente http y la URL obtenida desde propiedades.
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                // Dependencia para soportar parseo de JSON a estructuras de datos.
                gson()
            }
        }
        val url = BuildConfig.CONNECTION_TEST_URL

        try {
            // Con una solicitud correcta se mostrarán los datos de la respuesta en un TextView.
            val response: HttpResponse = client.get(url)
            val urlInfo = "URL:\n$url"
            val statusInfo = "Http response status:\n${response.status}"

            // Se parsea el JSON dentro de body a su clase correspondiente para acceder a sus campos.
            val user: User = response.body()

            // Se muestra la información obtenida.
            txtResponse.text = "$urlInfo\n$statusInfo\n\n$user"
        }
        // Manejo de excepciones de conectividad.
        catch (e: Exception) {
            txtResponse.text = "An exception occurred while doing the request"
            e.printStackTrace()
        }
        finally {
            // Liberación de recursos del ktor client.
            client.close()
            println("Ktor client closed")
        }
    }
}