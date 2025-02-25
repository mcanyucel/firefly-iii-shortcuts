package com.mustafacanyucel.fireflyiiishortcuts.di

import android.app.Activity
import com.mustafacanyucel.fireflyiiishortcuts.services.dialog.DialogService
import com.mustafacanyucel.fireflyiiishortcuts.services.dialog.IDialogService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object DialogModule {
    @Provides
    fun provideDialogService(
        activity: Activity
    ): IDialogService = DialogService(activity)
}