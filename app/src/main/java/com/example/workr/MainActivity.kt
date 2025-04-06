package com.example.workr

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
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
        txtResponse.text = "Cargando solicitud..."

        // Se lanza una corutina con la tarea de hacer la solicitud web
        // usando un Scope de corutinas no-bloqueante al nivel del ciclo de vida de la Activity.
        lifecycleScope.launch { doRequest() }
    }

    /**
     * Hace una solicitud HTTP y muestra el resultado con texto en la vista.
     */
    suspend fun doRequest() {
        // Se establece el endpoint al que se hará la solicitud
        val endpoint = "/users/test_http_client_api"
        // Se establece el metodo HTTP correspondiente al endpoint.
        val method = HttpMethod.Get

        try {
            // Con una solicitud correcta se mostrarán los datos de la respuesta en un TextView.
            val response: HttpResponse = HTTPClientAPI.makeRequest(endpoint, method)
            val endpoint = "Endpoint:\n$method $endpoint"
            val statusInfo = "Http response status:\n${response.status}"
            val body = "Http body:\n${response.bodyAsText()}"

            txtResponse.text = "$endpoint\n$statusInfo\n$body"
        }
        // Manejo de excepciones de conectividad.
        catch (e: ResponseException) {
            txtResponse.text = "Una excepción ocurrió al hacer la solicitud"
            e.printStackTrace()
        }
    }
}