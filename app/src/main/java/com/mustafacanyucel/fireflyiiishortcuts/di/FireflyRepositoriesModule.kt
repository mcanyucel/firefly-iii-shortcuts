package com.mustafacanyucel.fireflyiiishortcuts.di

import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalBillRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalBudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalCategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalPiggybankRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalShortcutRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalTagRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.IRemoteAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.IRemoteBillRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.IRemoteBudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.IRemoteCategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.IRemotePiggybankRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.IRemoteTagRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.RemoteFireflyRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.firefly.FireflyIiiApiService
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
     * Instead of injecting the repository directly, we inject all its dependencies.
     */
    @Provides
    @Singleton
    fun provideLocalFireflyRepository(
        localAccountRepository: ILocalAccountRepository,
        localBillRepository: ILocalBillRepository,
        localBudgetRepository: ILocalBudgetRepository,
        localCategoryRepository: ILocalCategoryRepository,
        localPiggybankRepository: ILocalPiggybankRepository,
        localTagRepository: ILocalTagRepository,
        localShortcutRepository: ILocalShortcutRepository
    ): LocalFireflyRepository {
        return LocalFireflyRepository(
            localAccountRepository,
            localBillRepository,
            localBudgetRepository,
            localCategoryRepository,
            localPiggybankRepository,
            localTagRepository,
            localShortcutRepository
        )
    }

    /**
     * Provides the RemoteFireflyRepository as a singleton.
     * Instead of injecting the repository directly, we inject all its dependencies.
     */
    @Provides
    @Singleton
    fun provideRemoteFireflyRepository(
        remoteAccountRepository: IRemoteAccountRepository,
        remoteBillRepository: IRemoteBillRepository,
        remoteBudgetRepository: IRemoteBudgetRepository,
        remoteCategoryRepository: IRemoteCategoryRepository,
        remotePiggybankRepository: IRemotePiggybankRepository,
        remoteTagRepository: IRemoteTagRepository,
        apiService: FireflyIiiApiService
    ): RemoteFireflyRepository {
        return RemoteFireflyRepository(
            remoteAccountRepository,
            remoteBillRepository,
            remoteBudgetRepository,
            remoteCategoryRepository,
            remotePiggybankRepository,
            remoteTagRepository,
            apiService
        )
    }
}