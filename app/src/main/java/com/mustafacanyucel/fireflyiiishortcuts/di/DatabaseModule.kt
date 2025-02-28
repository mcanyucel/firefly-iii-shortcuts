package com.mustafacanyucel.fireflyiiishortcuts.di

import android.content.Context
import com.mustafacanyucel.fireflyiiishortcuts.data.AppDatabase
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.AccountDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.BillDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.BudgetDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.CategoryDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.PiggybankDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideAccountDao(database: AppDatabase): AccountDao {
        return database.accountDao()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideBudgetDao(database: AppDatabase): BudgetDao {
        return database.budgetDao()
    }

    @Provides
    fun provideTagDao(database: AppDatabase): TagDao {
        return database.tagDao()
    }

    @Provides
    fun providePiggybankDao(database: AppDatabase): PiggybankDao {
        return database.piggybankDao()
    }

    @Provides
    fun provideBillDao(database: AppDatabase): BillDao {
        return database.billDao()
    }
}