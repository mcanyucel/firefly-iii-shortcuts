package com.mustafacanyucel.fireflyiiishortcuts.di

import com.mustafacanyucel.fireflyiiishortcuts.data.repository.ILocalAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.ILocalBudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.ILocalCategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.ILocalPiggybankRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.ILocalTagRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.LocalAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.LocalBudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.LocalCategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.LocalPiggybankRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.LocalTagRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.AccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.BudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.CategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.IAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.IBudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.ICategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.IPiggybankRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.ITagRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.PiggybankRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.TagRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        repo: AccountRepository
    ): IAccountRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        repo: CategoryRepository
    ): ICategoryRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        repo: BudgetRepository
    ): IBudgetRepository

    @Binds
    @Singleton
    abstract fun bindTagRepository(
        repo: TagRepository
    ): ITagRepository

    @Binds
    @Singleton
    abstract fun bindPiggybankRepository(
        repository: PiggybankRepository
    ): IPiggybankRepository

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

}