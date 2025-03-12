package com.mustafacanyucel.fireflyiiishortcuts.services.auth

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.net.toUri
import com.mustafacanyucel.fireflyiiishortcuts.MainActivity
import com.mustafacanyucel.fireflyiiishortcuts.services.preferences.IPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.CodeVerifierUtil
import net.openid.appauth.ResponseTypeValues
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * OAuth2 manager that handles the authentication with a Firefly III server.
 * It is designed as a singleton that can be injected into View-Models.
 */
@Singleton
class Oauth2Manager @Inject constructor(
    private val preferencesRepository: IPreferencesRepository,
    @ApplicationContext private val appContext: Context
) {

    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _authState = MutableStateFlow<AuthState?>(null)
    private lateinit var _clientId: String
    private lateinit var _serverUrl: String
    private lateinit var _registeredRedirectUrl: String
    val authState = _authState.asStateFlow()

    private val authService by lazy { AuthorizationService(appContext) }

    init {
        managerScope.launch {
            loadAuthState()

            preferencesRepository.changedKeyFlow.collect { changedKey ->
                when (changedKey) {
                    preferencesRepository.serverUrlKey -> {
                        _serverUrl = preferencesRepository.getString(changedKey, "")
                    }

                    preferencesRepository.clientIdKey -> {
                        _clientId = preferencesRepository.getString(changedKey, "")
                    }

                    preferencesRepository.registeredRedirectUrl -> {
                        _registeredRedirectUrl = preferencesRepository.getString(changedKey, "")
                    }
                }
            }
        }
    }


    private suspend fun loadAuthState() {
        val stateJson = preferencesRepository.getString(AUTH_STATE, "")
        _clientId = preferencesRepository.getString(preferencesRepository.clientIdKey, "")
        _serverUrl =
            trimUrl(preferencesRepository.getString(preferencesRepository.serverUrlKey, ""))
        _registeredRedirectUrl = preferencesRepository.getString(
            preferencesRepository.registeredRedirectUrl,
            "https://fireflyiiishortcuts.mustafacanyucel.com/oauth2redirect.html"
        )
        val loadedState = if (stateJson.isNotEmpty()) {
            try {
                AuthState.jsonDeserialize(stateJson)
            } catch (e: Exception) {
                AuthState()
            }
        } else {
            AuthState()
        }

        _authState.value = loadedState
    }

    private suspend fun persistAuthState() {
        _authState.value?.let { state ->
            preferencesRepository.saveString(AUTH_STATE, state.jsonSerializeString())
        }
    }

    private fun trimUrl(url: String): String {
        // remove trailing / from serverUrl if it exists
        return if (url.endsWith("/")) {
            url.substring(0, url.length - 1)
        } else {
            url
        }
        // note that serverUrl will always start with HTTP:// or HTTPS://
    }

    private fun prepareAuthRequest(): AuthorizationRequest {

        val authEndpoint = "$_serverUrl$FIREFLY_AUTH_ENDPOINT"
        val tokenEndpoint = "$_serverUrl$FIREFLY_TOKEN_ENDPOINT"

        val serviceConfig = AuthorizationServiceConfiguration(
            authEndpoint.toUri(),
            tokenEndpoint.toUri()
        )

        val codeVerifier = CodeVerifierUtil.generateRandomCodeVerifier()
        val codeChallenge = CodeVerifierUtil.deriveCodeVerifierChallenge(codeVerifier)

        return AuthorizationRequest.Builder(
            serviceConfig,
            _clientId,
            ResponseTypeValues.CODE,
            _registeredRedirectUrl.toUri()
        )
            .setCodeVerifier(
                codeVerifier,
                codeChallenge,
                "S256"
            )
            .build()
    }

    private fun updateAuthState(newState: AuthState) {
        _authState.value = newState
        managerScope.launch {
            persistAuthState()
        }
    }

    fun startAuthorizationFlow(activity: Activity, requestCode: Int) {
        val authRequest = prepareAuthRequest()

        Log.d("Oauth2Manager", "Starting OAuth flow with request: ${authRequest.state}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // For Android 12+, we need FLAG_MUTABLE for PendingIntents used in OAuth
            startAuthWithPendingIntents(
                activity,
                authRequest,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            // For older versions
            startAuthWithPendingIntents(activity, authRequest, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    private fun startAuthWithPendingIntents(
        activity: Activity,
        authRequest: AuthorizationRequest,
        flags: Int
    ) {
        // Create completion intent (to handle success)
        val completionIntent = Intent(activity, MainActivity::class.java)
        completionIntent.action = Intent.ACTION_VIEW
        completionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Create cancellation intent (to handle cancellation/failure)
        val cancelIntent = Intent(activity, MainActivity::class.java)
        cancelIntent.action = Intent.ACTION_VIEW
        cancelIntent.putExtra("oauth_canceled", true)
        cancelIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Launch the authorization request with PendingIntents
        authService.performAuthorizationRequest(
            authRequest,
            PendingIntent.getActivity(activity, 0, completionIntent, flags),
            PendingIntent.getActivity(activity, 0, cancelIntent, flags)
        )
    }

    suspend fun handleAuthorizationResponse(intent: Intent): Boolean {
        try {
            // Check for cancellation
            /// FOR SOME REASON, THE EXTRA IS ALWAYS TRUE EVEN IF WE GET A CODE
//            if (intent.getBooleanExtra("oauth_canceled", false)) {
//                Log.d("Oauth2Manager", "OAuth flow was canceled")
//                return false
//            }

            // Extract the response and exception from the intent
            val response = AuthorizationResponse.fromIntent(intent)
            val exception = AuthorizationException.fromIntent(intent)

            Log.d("Oauth2Manager", "Authorization response: $response")
            if (exception != null) {
                Log.e("Oauth2Manager", "Authorization exception: ${exception.message}", exception)
                return false
            }

            // If there's no response, it's a failure
            if (response == null) {
                Log.e("Oauth2Manager", "No authorization response in intent")
                return false
            }

            // Exchange authorization code for tokens
            return suspendCancellableCoroutine { continuation ->
                val tokenRequest = response.createTokenExchangeRequest()
                Log.d(
                    "Oauth2Manager",
                    "Performing token request: ${tokenRequest.jsonSerializeString()}"
                )

                authService.performTokenRequest(tokenRequest) { tokenResponse, tokenException ->
                    if (tokenException != null) {
                        Log.e(
                            "Oauth2Manager",
                            "Token exchange error: ${tokenException.message}",
                            tokenException
                        )
                        continuation.resume(false)
                        return@performTokenRequest
                    }

                    if (tokenResponse == null) {
                        Log.e("Oauth2Manager", "Token response is null")
                        continuation.resume(false)
                        return@performTokenRequest
                    }

                    Log.d("Oauth2Manager", "Token exchange successful")

                    // Create and update auth state
                    val authState = AuthState(response, exception)
                    authState.update(tokenResponse, tokenException)
                    updateAuthState(authState)

                    continuation.resume(true)
                }
            }
        } catch (e: Exception) {
            Log.e("Oauth2Manager", "Error processing authorization response", e)
            return false
        }
    }

    fun handleManualAuthCode(code: String, state: String?): Boolean {
        try {
            // Create service configuration
            val serviceConfig = AuthorizationServiceConfiguration(
                "$_serverUrl$FIREFLY_AUTH_ENDPOINT".toUri(),
                "$_serverUrl$FIREFLY_TOKEN_ENDPOINT".toUri()
            )

            // Create a fake authorization response to use for token exchange
            val redirectUri = _registeredRedirectUrl.toUri()
            val request = AuthorizationRequest.Builder(
                serviceConfig,
                _clientId,
                ResponseTypeValues.CODE,
                redirectUri
            ).setState(state ?: "").build()

            val response = AuthorizationResponse.Builder(request)
                .setAuthorizationCode(code)
                .setState(state ?: "")
                .build()

            // Create token exchange request
            val tokenRequest = response.createTokenExchangeRequest()

            // Now perform the token exchange
            val result = runBlocking {
                suspendCancellableCoroutine { continuation ->
                    authService.performTokenRequest(tokenRequest) { tokenResponse, tokenException ->
                        if (tokenException != null || tokenResponse == null) {
                            Log.e("OAuth2Manager", "Token exchange failed", tokenException)
                            continuation.resume(false)
                        } else {
                            // Create and save AuthState
                            val authState = AuthState(response, null)
                            authState.update(tokenResponse, null)
                            _authState.value = authState

                            // Save auth state to persistent storage
                            CoroutineScope(Dispatchers.IO).launch {
                                persistAuthState()
                            }

                            continuation.resume(true)
                        }
                    }
                }
            }

            return result
        } catch (e: Exception) {
            Log.e("OAuth2Manager", "Error in manual code handling", e)
            return false
        }
    }


    fun logout() {
        _authState.value = AuthState()
        managerScope.launch {
            persistAuthState()
        }
    }

    fun dispose() {
        authService.dispose()
    }

    companion object {
        private const val FIREFLY_AUTH_ENDPOINT =
            "/oauth/authorize"
        private const val FIREFLY_TOKEN_ENDPOINT = "/oauth/token"
        private const val AUTH_STATE = "auth_state"

        const val RC_AUTH = 100
    }

}