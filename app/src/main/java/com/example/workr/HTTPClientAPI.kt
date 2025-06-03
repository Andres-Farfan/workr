package com.example.workr

import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.plugin
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.defaultForFile
import io.ktor.http.isSecure
import io.ktor.serialization.gson.gson
import io.ktor.utils.io.InternalAPI
import java.io.File

/**
 * Objeto global utilizado para encapsular la lógica de solicitudes HTTP al backend.
 */
object HTTPClientAPI {
    private val BACKEND_BASE_URL = BuildConfig.BACKEND_BASE_URL
    private val BACKEND_API_KEY = BuildConfig.BACKEND_API_KEY

    private val gson = Gson()

    private var jwt: String? = null

    /**
     * Configura el JWT a usar en las requests.
     * @param jwt JWT que se usará para autenticación de las requests.
     */
    fun setJwt(jwt: String) {
        this.jwt = jwt
    }

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
            // Se agrega la api key del backend por defecto a toda solicitud.
            install(DefaultRequest) {
                if (jwt != null) {
                    header(HttpHeaders.Authorization, "Bearer $jwt")
                }
                header("Api-Key", BACKEND_API_KEY)
            }
        }
        client.plugin(HttpSend).intercept { request ->
            val url = request.url
            val isLocalHost = url.host == "10.0.2.2"
            val isSecure = url.protocol.isSecure()

            if (!isSecure && !isLocalHost) {
                throw IllegalStateException("Sólo se permiten solicitudes HTTPS fuera de localhost")
            }
            execute(request)
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

    /**
     * Envía una solicitud multipart configurable.
     * @param endpoint Endpoint al que se hará la solicitud.
     * @param method Metodo HTTP del endpoint.
     * @param headers Headers opcionales que se incluirán en la solicitud
     * @param formFields Campos del formulario:
     * Para texto se usa par clave-valor (String to String)
     * Para archivos se usa par clave-File (String to File)
     * @return HttpResponse del servidor
     */
    // Anotación para evitar un error conocido que impide usar el constructor de headers.
    @OptIn(InternalAPI::class)
    suspend fun submitMultipartForm(
        endpoint: String,
        formFields: Map<String, Any>
    ): HttpResponse {
        // Para el entorno de desarrollo, se usa un nuevo HTTPClient por cada solicitud.
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                // Dependencia para soportar parseo de JSON a estructuras de datos.
                gson()
            }
            // Se agrega la api key del backend por defecto a toda solicitud.
            install(DefaultRequest) {
                if (jwt != null) {
                    header(HttpHeaders.Authorization, "Bearer $jwt")
                }
                header("Api-Key", BACKEND_API_KEY)
            }
        }
        client.plugin(HttpSend).intercept { request ->
            val url = request.url
            val isLocalHost = url.host == "10.0.2.2"
            val isSecure = url.protocol.isSecure()

            if (!isSecure && !isLocalHost) {
                throw IllegalStateException("Sólo se permiten solicitudes HTTPS fuera de localhost")
            }
            execute(request)
        }
        // Log para entorno de desarrollo.
        println("Ktor HTTP Client abierto")

        // Ejecución de la solicitud con los parámetros proporcionados.
        try {
            // Se completa la URL de una solicitud combinando la URL base del backend
            // con el endpoint indicado en parámetros.
            val url = "$BACKEND_BASE_URL$endpoint"

            return client.submitFormWithBinaryData(
                url = url,
                formData = formData {
                    for ((key, value) in formFields) {
                        when (value) {
                            is String -> {
                                append(key, value)
                            }
                            is File -> {
                                append(
                                    key,
                                    value.readBytes(),
                                    Headers.build {
                                        append(HttpHeaders.ContentType, ContentType.defaultForFile(value).toString())
                                        append(HttpHeaders.ContentDisposition, "filename=\"${value.name}\"")
                                    }
                                )
                            }
                            is Map<*, *> -> {
                                val json = gson.toJson(value)
                                append(key, json)
                            }
                            is List<*> -> {
                                val json = gson.toJson(value)
                                append(key, json)
                            }
                            else -> {
                                throw IllegalArgumentException("Tipo no soportado para '$key': ${value::class}")
                            }
                        }
                    }
                }
            )
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