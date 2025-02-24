package com.mustafacanyucel.fireflyiiishortcuts.services.auth

import android.content.Context
import android.net.Uri
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.CodeVerifierUtil
import net.openid.appauth.ResponseTypeValues

class Oauth2Manager(private val context: Context) {
    private var authService: AuthorizationService = AuthorizationService(context)
    private lateinit var authState: AuthState

    init {
        loadAuthState()
    }

    private fun loadAuthState() {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val stateJson = prefs.getString(AUTH_STATE, null)
        authState = if (stateJson != null) {
            AuthState.jsonDeserialize(stateJson)
        }
        else {
            AuthState()
        }
    }

    private fun persistAuthState() {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(AUTH_STATE, authState.jsonSerializeString())
            .apply()
    }

    fun startAuth() {
        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse(FIREFLY_AUTH_ENDPOINT),
            Uri.parse(FIREFLY_TOKEN_ENDPOINT)
        )

        val codeVerifier = CodeVerifierUtil.generateRandomCodeVerifier()
        val codeChallenge = CodeVerifierUtil.deriveCodeVerifierChallenge(codeVerifier)

        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(REGISTERED_REDIRECT_URI)
        )
            .setCodeVerifier(
                codeVerifier,
                codeChallenge,
                "SHA256"
            )
            .build()

        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
    }



    companion object {
        private const val FIREFLY_AUTH_ENDPOINT = "https://budget.mustafacanyucel.com/oauth/authorize"
        private const val FIREFLY_TOKEN_ENDPOINT = "https://budget.mustafacanyucel.com/oauth/token"
        private const val CLIENT_ID = "3"
        private const val REGISTERED_REDIRECT_URI = "http://127.0.0.1/callback"
        private const val APP_SCHEME = "com.mustafacanyucel.fireflyiiishortcuts"
        private const val APP_REDIRECT_URI = "$APP_SCHEME:/oauth2callback"
        private const val PREFS_NAME = "OAuth2State"
        private const val AUTH_STATE = "auth_state"
    }

}