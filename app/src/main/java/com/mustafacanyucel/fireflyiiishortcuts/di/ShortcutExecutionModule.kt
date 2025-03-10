package com.mustafacanyucel.fireflyiiishortcuts.di

import android.app.Application
import com.mustafacanyucel.fireflyiiishortcuts.services.firefly.ShortcutExecutionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShortcutExecutionModule {
    @Provides
    @Singleton
    fun provideShortcutExecutionRepository(application: Application): ShortcutExecutionRepository {
        return ShortcutExecutionRepository(application)
    }
}