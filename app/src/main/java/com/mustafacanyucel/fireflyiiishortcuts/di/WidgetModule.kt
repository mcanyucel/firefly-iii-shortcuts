package com.mustafacanyucel.fireflyiiishortcuts.di

import android.content.Context
import com.mustafacanyucel.fireflyiiishortcuts.data.AppDatabase
import com.mustafacanyucel.fireflyiiishortcuts.widget.ShortcutWidgetExecutor
import com.mustafacanyucel.fireflyiiishortcuts.widget.ShortcutWidgetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WidgetModule {

    @Provides
    @Singleton
    fun provideShortcutWidgetRepository(
        @ApplicationContext context: Context
    ): ShortcutWidgetRepository {
        return ShortcutWidgetRepository(context)
    }

    @Provides
    @Singleton
    fun provideShortcutWidgetExecutor(
        @ApplicationContext context: Context,
        database: AppDatabase,
        widgetRepository: ShortcutWidgetRepository
    ): ShortcutWidgetExecutor {
        return ShortcutWidgetExecutor(context, database, widgetRepository)
    }
}