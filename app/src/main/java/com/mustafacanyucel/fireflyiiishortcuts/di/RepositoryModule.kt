package com.mustafacanyucel.fireflyiiishortcuts.di

import com.mustafacanyucel.fireflyiiishortcuts.data.repository.ILocalAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.ILocalCategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.LocalAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.data.repository.LocalCategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.AccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.BudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.CategoryRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.IAccountRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.IBudgetRepository
import com.mustafacanyucel.fireflyiiishortcuts.services.repository.ICategoryRepository
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
    abstract fun bindLocalAccountRepository(
        repo: LocalAccountRepository
    ): ILocalAccountRepository

    @Binds
    @Singleton
    abstract fun bindLocalCategoryRepository(
        repo: LocalCategoryRepository
    ): ILocalCategoryRepository
}