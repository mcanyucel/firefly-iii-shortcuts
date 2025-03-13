package com.mustafacanyucel.fireflyiiishortcuts.di

import android.content.Context
import com.mustafacanyucel.fireflyiiishortcuts.version.VersionUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VersionUtilModule {
    @Provides
    @Singleton
    fun provideVersionUtil(
        @ApplicationContext applicationContext: Context
    ): VersionUtil {
        return VersionUtil(applicationContext)
    }
}