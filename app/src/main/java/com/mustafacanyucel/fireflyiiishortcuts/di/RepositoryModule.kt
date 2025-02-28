package com.mustafacanyucel.fireflyiiishortcuts.di

import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalBillRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalBudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalCategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalPiggybankRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalShortcutRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.ILocalTagRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalBillRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalBudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalCategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalPiggybankRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalShortcutRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.local.LocalTagRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.IRemoteAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.IRemoteBillRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.IRemoteBudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.IRemoteCategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.IRemotePiggybankRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.IRemoteTagRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.RemoteAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.RemoteBillRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.RemoteBudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.RemoteCategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.RemotePiggybankRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote.RemoteTagRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // Remote Repository Bindings
    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        repo: RemoteAccountRepository
    ): IRemoteAccountRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        repo: RemoteCategoryRepository
    ): IRemoteCategoryRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        repo: RemoteBudgetRepository
    ): IRemoteBudgetRepository

    @Binds
    @Singleton
    abstract fun bindTagRepository(
        repo: RemoteTagRepository
    ): IRemoteTagRepository

    @Binds
    @Singleton
    abstract fun bindPiggybankRepository(
        repository: RemotePiggybankRepository
    ): IRemotePiggybankRepository

    @Binds
    @Singleton
    abstract fun bindBillRepository(
        repo: RemoteBillRepository
    ): IRemoteBillRepository

    // Local Repository Bindings
    @Binds
    @Singleton
    abstract fun bindLocalAccountRepository(
        repo: LocalAccountRepository
    ): ILocalAccountRepository

    @Binds
    @Singleton
    abstract fun bindLocalCategoryRepository(
        repo: LocalCategoryRepository
    ): ILocalCategoryRepository

    @Binds
    @Singleton
    abstract fun bindLocalBudgetRepository(
        repo: LocalBudgetRepository
    ): ILocalBudgetRepository

    @Binds
    @Singleton
    abstract fun bindLocalTagRepository(
        repo: LocalTagRepository
    ): ILocalTagRepository

    @Binds
    @Singleton
    abstract fun bindLocalPiggybankRepository(
        repo: LocalPiggybankRepository
    ): ILocalPiggybankRepository

    @Binds
    @Singleton
    abstract fun bindLocalBillRepository(
        repo: LocalBillRepository
    ): ILocalBillRepository

    @Binds
    @Singleton
    abstract fun bindLocalShortcutRepository(
        repo: LocalShortcutRepository
    ): ILocalShortcutRepository
}