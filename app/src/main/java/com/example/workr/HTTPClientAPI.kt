package com.example.workr

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson

/**
 * Objeto global utilizado para encapsular la lógica de solicitudes HTTP al backend.
 */
object HTTPClientAPI {
    private val BACKEND_BASE_URL = BuildConfig.BACKEND_BASE_URL

    /**
     * Realiza una solicitud HTTP al endpoint especificado del backend.
     * @param endpoint Endpoint al que se hará la solicitud.
     * @param method Método HTTP correspondiente del endpoint, por defecto GET.
     * @param body Body opcional de una solicitud.
     * @param headers Headers opcionales de una solicitud.
     */
    suspend fun makeRequest(
        endpoint: String,
        method: HttpMethod = HttpMethod.Get,
        body: Any? = null,
        headers: Map<String, String> = emptyMap()
    ): HttpResponse {
        // Para el entorno de desarrollo, se usa un nuevo HTTPClient por cada solicitud.
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                // Dependencia para soportar parseo de JSON a estructuras de datos.
                gson()
            }
        }
        // Log para entorno de desarrollo.
        println("Ktor HTTP Client abierto")

        // Ejecución de la solicitud con los parámetros proporcionados.
        try {
            // Se completa la URL de una solicitud combinando la URL base del backend
            // con el endpoint indicado en parámetros.
            val url = "$BACKEND_BASE_URL$endpoint"

            // Si se completa adecuadamente la solicitud, se obtendrá un HTTPResponse.
            return client.request(url) {
                this.method = method
                if (body != null) {
                    this.contentType(ContentType.Application.Json)
                    setBody(body)
                }
                headers.forEach { (key, value) -> this.headers.append(key, value) }
            }
        }
        // Si ocurre un error en la solicitud, se lanzará una excepción para interrumpir otras operaciones.
        catch (e: ResponseException) {
            throw e
        }
        // Para el entorno de desarrollo, el HTTPClient se cierra después de cada solicitud.
        finally {
            client.close()
            // Log para entorno de desarrollo.
            println("Ktor HTTP Client cerrado")
        }
    }
}