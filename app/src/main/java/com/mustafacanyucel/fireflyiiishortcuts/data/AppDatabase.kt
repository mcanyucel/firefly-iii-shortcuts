package com.mustafacanyucel.fireflyiiishortcuts.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.AccountDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.BillDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.BudgetDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.CategoryDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.PiggybankDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.ShortcutDao
import com.mustafacanyucel.fireflyiiishortcuts.data.dao.TagDao
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.ShortcutTagCrossRef
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.TagEntity

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        BudgetEntity::class,
        TagEntity::class,
        PiggybankEntity::class,
        BillEntity::class,
        ShortcutEntity::class,
       ShortcutTagCrossRef::class],
    version = 10,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun tagDao(): TagDao
    abstract fun piggybankDao(): PiggybankDao
    abstract fun billDao(): BillDao
    abstract fun shortcutDao(): ShortcutDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "firefly_shortcuts_db"
                ).fallbackToDestructiveMigration() // For simplicity in development
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}