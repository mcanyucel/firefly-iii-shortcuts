package com.mustafacanyucel.fireflyiiishortcuts.services.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mustafacanyucel.fireflyiiishortcuts.services.preferences.IPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
        }
    }

    private suspend fun loadAuthState() {
        val stateJson = preferencesRepository.getString(AUTH_STATE, "")
        _clientId = preferencesRepository.getString(preferencesRepository.clientIdKey, "")
        _serverUrl = trimUrl(preferencesRepository.getString(preferencesRepository.serverUrlKey, ""))
        _registeredRedirectUrl = preferencesRepository.getString(
            preferencesRepository.registeredRedirectUrl,
            "https://fireflyiiishortcuts.mustafacanyucel.com/oauth2redirect"
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

    fun prepareAuthRequest(): AuthorizationRequest {

        val authEndpoint = "$_serverUrl$FIREFLY_AUTH_ENDPOINT"
        val tokenEndpoint = "$_serverUrl$FIREFLY_TOKEN_ENDPOINT"

        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse(authEndpoint),
            Uri.parse(tokenEndpoint)
        )

        val codeVerifier = CodeVerifierUtil.generateRandomCodeVerifier()
        val codeChallenge = CodeVerifierUtil.deriveCodeVerifierChallenge(codeVerifier)

        return AuthorizationRequest.Builder(
            serviceConfig,
            _clientId,
            ResponseTypeValues.CODE,
            Uri.parse(_registeredRedirectUrl)
        )
            .setCodeVerifier(
                codeVerifier,
                codeChallenge,
                "SHA256"
            )
            .build()
    }

    fun createAuthorizationBrowserIntent(): Intent {
        val authRequest = prepareAuthRequest()
        return Intent(Intent.ACTION_VIEW, authRequest.toUri())
    }

    fun updateAuthState(newState: AuthState) {
        _authState.value = newState
        managerScope.launch {
            persistAuthState()
        }
    }

    fun isAuthenticated(): Boolean {
        return _authState.value?.isAuthorized == true
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun handleAuthorizationResponse(uri: Uri): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, uri)
            val response = AuthorizationResponse.fromIntent(intent)
            val exception = AuthorizationException.fromIntent(intent)

            val newState = AuthState(response, exception)
            updateAuthState(newState)

            // if there is a response, start token exchange
            if (response != null && exception == null) {
                val tokenRequestResult = suspendCancellableCoroutine { cancellableContinuation ->
                    val tokenRequest = response.createTokenExchangeRequest()
                    val disposable =
                        authService.performTokenRequest(tokenRequest) { tokenResponse, tokenException ->
                            val updatedState = AuthState(response, exception)
                            updatedState.update(tokenResponse, tokenException)
                            updateAuthState(updatedState)

                            val success = tokenResponse != null && tokenException == null
                            cancellableContinuation.resume(success)
                        }

                    // handle cancellation
                    cancellableContinuation.invokeOnCancellation {
                        // cleanup, if any
                    }
                }

                tokenRequestResult
            } else {
                false
            }
        }
        catch (e: Exception) {
            false
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
    }

}