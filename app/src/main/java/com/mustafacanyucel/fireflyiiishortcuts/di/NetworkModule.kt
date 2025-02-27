package com.mustafacanyucel.fireflyiiishortcuts.di

import com.mustafacanyucel.fireflyiiishortcuts.model.api.AuthInterceptor
import com.mustafacanyucel.fireflyiiishortcuts.services.api.ConnectionDebugInterceptor
import com.mustafacanyucel.fireflyiiishortcuts.services.api.DebugInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        debugInterceptor: DebugInterceptor,
        connectionDebugInterceptor: ConnectionDebugInterceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(connectionDebugInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(debugInterceptor.createLoggingInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}