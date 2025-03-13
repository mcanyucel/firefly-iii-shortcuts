package com.mustafacanyucel.fireflyiiishortcuts.version

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VersionUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val versionName: String
        get() = try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }

    val versionCode: Long
        get() = try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.longVersionCode
        } catch (e: Exception) {
            -1L
        }

    val versionInfo: String
        get() = "App Version: $versionName (Build Code: $versionCode)"
}