package com.mustafacanyucel.fireflyiiishortcuts.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.AccountDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.BudgetDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.CategoryDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity

@Database(
    entities = [AccountEntity::class, CategoryEntity::class, BudgetEntity::class],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "firefly_shortcuts_db"
                )
                    .fallbackToDestructiveMigration() // For simplicity in development
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}