package com.mustafacanyucel.fireflyiiishortcuts.services.api

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebugInterceptor @Inject constructor() {

    fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Log.d("FireflyAPI", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}
