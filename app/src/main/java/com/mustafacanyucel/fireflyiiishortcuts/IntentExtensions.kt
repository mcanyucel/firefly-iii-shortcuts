package com.mustafacanyucel.fireflyiiishortcuts

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.io.Serializable

/**
 * Gets a parcelable extra from an intent, handling API level differences.
 *
 * @param key The extra key
 * @return The parcelable object or null
 */
inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableExtra(key)
    }
}

/**
 * Gets a serializable extra from an intent, handling API level differences.
 *
 * @param key The extra key
 * @return The serializable object or null
 */
inline fun <reified T : Serializable> Intent.getSerializableExtraCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(key) as? T
    }
}