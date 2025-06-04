package com.example.workr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.ktor.client.call.body
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfficeGroupCallActivity : AppCompatActivity() {
    // Identificador de la solicitud de permisos para llamar.
    companion object {
        private const val PERMISSION_REQ_ID = 22
    }

    // Variables de configuración del servicio de llamadas.
    private val callingAppId = BuildConfig.CALLING_APP_ID
    private var mRtcEngine: RtcEngine? = null

    // Parámetros del intent.
    private lateinit var userId: String
    private lateinit var jwt: String
    private lateinit var companyId: String

    // Datos obtenidos del backend para llamada.
    private lateinit var directory: Map<String, OfficeUser>
    private val inCallUsers = mutableStateListOf<OfficeUser>()
    private lateinit var token: String
    private var callUserId = 0

    // Variable y metodo que garantizan pasar por la limpieza y cierre de la activity una sola vez,
    // a pesar de tener varias solicitudes para ello.
    private var isFinishing = false
    @Synchronized
    private fun concurrentSafeFinish() {
        if (isFinishing) return
        isFinishing = true
        runOnUiThread {
            finish()
        }
    }

    // Manejador de eventos en la llamada que actualiza la lista de usuarios conectados y
    // y maneja posibles errores.
    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            runOnUiThread {
                val user = directory["$uid"]
                if (user != null && !inCallUsers.contains(user)) {
                    inCallUsers.add(user)
                }
            }
        }

        override fun onUserJoined(uid: Int, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            runOnUiThread {
                val user = directory["$uid"]
                if (user != null && !inCallUsers.contains(user)) {
                    inCallUsers.add(user)
                }
            }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            super.onUserOffline(uid, reason)
            runOnUiThread {
                val user = directory["$uid"]
                if (user != null && inCallUsers.contains(user)) {
                    inCallUsers.remove(user)
                }
            }
        }

        override fun onTokenPrivilegeWillExpire(token: String?) {
            super.onTokenPrivilegeWillExpire(token)

            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(applicationContext, "Los privilegios de llamada expirarán pronto", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onRequestToken() {
            super.onRequestToken()

            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(applicationContext, "No hay renovación ni cambios de token, cerrando activity", Toast.LENGTH_SHORT).show()
            }
            concurrentSafeFinish()
        }

        override fun onError(err: Int) {
            super.onError(err)

            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(applicationContext, "Error del engine de llamada, Código: $err", Toast.LENGTH_SHORT).show()
            }
            concurrentSafeFinish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se verifica la configuración de la propiedad de configuración.
        if (callingAppId.isEmpty()) {
            throw Exception("Configuración incorrecta de calling app id")
        }

        // Se obtiene el intent y todos sus datos necesarios para conectar a la llamada.
        val intent = getIntent()
        if (intent == null) {
            throw Exception("No se pudo leer el intent para llamada")
        }
        userId = intent.getStringExtra("user_id").orEmpty()
        if (userId == "") {
            throw Exception("No se obtuvo un userId en el intent para llamada")
        }
        jwt = intent.getStringExtra("jwt").orEmpty()
        if (jwt == "") {
            throw Exception("No se obtuvo un jwt en el intent para llamada")
        }
        companyId = intent.getStringExtra("company_id").orEmpty()
        if (companyId == "") {
            throw Exception("No se obtuvo un companyId en el intent para llamada")
        }

        // Se configura la UI de Jetpack Compose.
        setContent {
            OfficeCallScreen(
                inCallUsers = inCallUsers,
                onMicToggle = { isMicOn ->
                    mRtcEngine?.muteLocalAudioStream(!isMicOn)
                },
                onExitPressed = {
                    concurrentSafeFinish()
                }
            )
        }

        // Metodo usado por el launch posterior para manejar excepciones adecuadamente en la corutina.
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(applicationContext, "Error al configurar la conexión a llamada, cerrando activity", Toast.LENGTH_SHORT).show()
            }
            concurrentSafeFinish()
        }

        // Se obtienen asíncronamente los datos que requieren solicitudes al backend
        // para poder conectar a la llamada.
        lifecycleScope.launch(coroutineExceptionHandler) {
            try {
                withContext(Dispatchers.Main) {
                    getCompanyCallDirectory()
                    getUsersInCall()
                    getCallToken()

                    if (checkPermissions()) {
                        startCalling()
                    } else {
                        requestPermissions()
                    }
                }
            }
            // Si ocurre un error mientras se accede a la llamada, se
            // terminará la activity, limpiando recursos abiertos.
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Error al configurar la conexión a llamada, cerrando activity", Toast.LENGTH_SHORT).show()
                }
                concurrentSafeFinish()
            }
        }
    }

    /**
     * Obtiene el "directorio" de llamadas de la empresa, que incluye datos de presentación
     * para listar de cada empleado indexados por su id de llamadas.
     */
    private suspend fun getCompanyCallDirectory() {
        val response = HTTPClientAPI.makeRequest(
            "calls/company_call_directory",
            method = HttpMethod.Get,
            headers = mapOf(
                HttpHeaders.Authorization to "Bearer $jwt"
            ),
            body = mapOf(
                "companyId" to companyId
            )
        )

        if (response.status == HttpStatusCode.OK) {
            val body: Map<String, Map<String, OfficeUser>> = response.body()
            this.directory = body["directory"]!!
        }
        else {
            throw Error("Error al obtener directorio de la empresa")
        }
    }

    /**
     * Obtiene la lista de usuarios conectados a la llamada actualmente.
     */
    private suspend fun getUsersInCall() {
        val response = HTTPClientAPI.makeRequest(
            "calls/users_in_call",
            method = HttpMethod.Get,
            headers = mapOf(
                HttpHeaders.Authorization to "Bearer $jwt"
            ),
            body = mapOf(
                "companyId" to companyId
            )
        )

        if (response.status == HttpStatusCode.OK) {
            val body: Map<String, List<Int>> = response.body()
            inCallUsers.addAll(
                (body["users"]!!).map { uid ->
                    directory["$uid"]!!
                }
            )
        }
        else {
            throw Error("Error al obtener usuarios activos")
        }
    }

    /**
     * Solicita las credenciales de autenticación para unirse a la llamada.
     */
    private suspend fun getCallToken() {
        val response = HTTPClientAPI.makeRequest(
            "calls/access_token",
            method = HttpMethod.Get,
            headers = mapOf(
                HttpHeaders.Authorization to "Bearer $jwt"
            ),
            body = mapOf(
                "companyId" to companyId
            )
        )

        if (response.status == HttpStatusCode.OK) {
            val body: Map<String, String> = response.body()
            this.token = body["token"]!!
            this.callUserId = Integer.parseInt(body["callUserId"]!!)
        }
        else {
            throw Error("Error al obtener token")
        }
    }

    /**
     * Solicita al usuario activar los permisos necesarios para llamada.
     */
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, getRequiredPermissions(), PERMISSION_REQ_ID)
    }

    /**
     * Revisa un posible estado inicial de permisos ya aceptados para llamada.
     */
    private fun checkPermissions(): Boolean {
        return getRequiredPermissions().all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Lista los permisos necesarios de aceptar para llamada.
     */
    private fun getRequiredPermissions(): Array<String> {
        return arrayOf(Manifest.permission.RECORD_AUDIO)
    }

    /**
     * Obtiene el resultado de la solicitud de permisos y une al usuario a llamada
     * sólo si los permisos fueron aceptados.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startCalling()
            }
            else {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(applicationContext, "No se concedieron permisos, cerrando activity", Toast.LENGTH_SHORT).show()
                }
                concurrentSafeFinish()
            }
        }
    }

    /**
     * Une al usuario a llamada configurando los atributos del motor del servicio.
     */
    private fun startCalling() {
        initializeAgoraVideoSDK()
        joinChannel()
    }

    /**
     * Configura el motor del servicio de llamadas.
     */
    private fun initializeAgoraVideoSDK() {
        try {
            val config = RtcEngineConfig().apply {
                mContext = applicationContext
                mAppId = callingAppId
                mEventHandler = mRtcEventHandler
            }
            mRtcEngine = RtcEngine.create(config)
        } catch (e: Exception) {
            throw RuntimeException("Error initializing RTC engine: ${e.message}")
        }
    }

    /**
     * Une al usuario a llamada definiendo una configuración para intercambio de
     * streams de medios, sin video y para empezar con audio en mute.
     */
    private fun joinChannel() {
        if (callUserId == 0) {
            throw Exception("No se obtuvo un id para llamada correcto")
        }

        val options = ChannelMediaOptions().apply {
            clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
            publishMicrophoneTrack = true
            publishCameraTrack = false
        }

        mRtcEngine?.muteLocalAudioStream(true)
        mRtcEngine?.disableVideo()
        mRtcEngine?.joinChannel(token, companyId, callUserId, options)
    }

    /**
     * Limpia la configuración del motor del servicio de llamadas antes de cerrar la activity.
     */
    override fun onDestroy() {
        super.onDestroy()

        cleanupAgoraEngine()
    }

    /**
     * Abandona la llamada y limpia la configuración del motor del servicio de llamadas.
     */
    private fun cleanupAgoraEngine() {
        try {
            mRtcEngine?.apply {
                leaveChannel()
            }
            RtcEngine.destroy()
            mRtcEngine = null
        }
        catch (e: Exception) {
            throw e
        }
    }
}