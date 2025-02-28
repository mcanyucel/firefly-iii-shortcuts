package com.mustafacanyucel.fireflyiiishortcuts.ui.bindingadapter

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.IEntity
import com.mustafacanyucel.fireflyiiishortcuts.ui.sync.SyncResult
import kotlin.reflect.KClass

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["syncResult", "entityClassForAdded"], requireAll = true)
fun TextView.setEntitiesAdded(syncResult: SyncResult?, entityClassName: String?) {
    if (syncResult == null || entityClassName == null) return

    val kClass = try {
        @Suppress("UNCHECKED_CAST") // nothing else can be given
        Class.forName(entityClassName).kotlin as KClass<out IEntity>
    } catch (e: ClassNotFoundException) {
        null
    }

    kClass?.let {
        text = syncResult.totalAdded(it).toString()
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["syncResult", "entityClassForUnchanged"], requireAll = true)
fun TextView.setEntitiesUnchanged(syncResult: SyncResult?, entityClassName: String?) {
    if (syncResult == null || entityClassName == null) return

    val kClass = try {
        @Suppress("UNCHECKED_CAST") // nothing else can be given
        Class.forName(entityClassName).kotlin as KClass<out IEntity>
    } catch (e: ClassNotFoundException) {
        null
    }

    kClass?.let {
        text = syncResult.totalUnchanged(it).toString()
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["syncResult", "entityClassForUpdated"], requireAll = true)
fun TextView.setEntitiesUpdated(syncResult: SyncResult?, entityClassName: String?) {
    if (syncResult == null || entityClassName == null) return

    val kClass = try {
        @Suppress("UNCHECKED_CAST") // nothing else can be given
        Class.forName(entityClassName).kotlin as KClass<out IEntity>
    } catch (e: ClassNotFoundException) {
        null
    }

    kClass?.let {
        text = syncResult.totalUpdated(it).toString()
    }
}


@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["syncResult", "entityClassForDeleted"], requireAll = true)
fun TextView.setEntitiesDeleted(syncResult: SyncResult?, entityClassName: String?) {
    if (syncResult == null || entityClassName == null) return

    val kClass = try {
        @Suppress("UNCHECKED_CAST") // nothing else can be given
        Class.forName(entityClassName).kotlin as KClass<out IEntity>
    } catch (e: ClassNotFoundException) {
        null
    }

    kClass?.let {
        text = syncResult.totalDeleted(it).toString()
    }
}