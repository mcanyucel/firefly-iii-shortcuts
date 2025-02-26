package com.mustafacanyucel.fireflyiiishortcuts.di

import android.content.Context
import com.mustafacanyucel.fireflyiiishortcuts.services.auth.Oauth2Manager
import com.mustafacanyucel.fireflyiiishortcuts.services.preferences.IPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Oauth2ManagerModule {
    @Provides
    @Singleton
    fun provideOauth2Manager(
        preferencesRepository: IPreferencesRepository,
        @ApplicationContext context: Context
    ): Oauth2Manager {
        return Oauth2Manager(preferencesRepository, context)
    }
}