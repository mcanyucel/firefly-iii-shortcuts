package com.mustafacanyucel.fireflyiiishortcuts.di

import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.RemoteFireflyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection module specifically for the unified Firefly repositories.
 * These repositories aggregate functionality from the underlying individual repositories.
 */
@Module
@InstallIn(SingletonComponent::class)
object FireflyRepositoriesModule {

    /**
     * Provides the LocalFireflyRepository as a singleton.
     * The constructor will automatically receive all required dependencies
     * that are already bound in the RepositoryModule.
     */
    @Provides
    @Singleton
    fun provideLocalFireflyRepository(localFireflyRepository: LocalFireflyRepository): LocalFireflyRepository {
        return localFireflyRepository
    }

    /**
     * Provides the RemoteFireflyRepository as a singleton.
     * The constructor will automatically receive all required dependencies
     * that are already bound in the RepositoryModule.
     */
    @Provides
    @Singleton
    fun provideRemoteFireflyRepository(remoteFireflyRepository: RemoteFireflyRepository): RemoteFireflyRepository {
        return remoteFireflyRepository
    }
}