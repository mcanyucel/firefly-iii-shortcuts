package com.mustafacanyucel.fireflyiiishortcuts.ui.sync

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.IEntity
import kotlin.reflect.KClass

data class SyncResult(
    val added: Map<KClass<out IEntity>, Int> = mapOf(),
    val updated: Map<KClass<out IEntity>, Int> = mapOf(),
    val deleted: Map<KClass<out IEntity>, Int> = mapOf(),
    val unchanged: Map<KClass<out IEntity>, Int> = mapOf(),
    val deletedShortcuts: Int = 0
) {
    fun totalAdded(entityType: KClass<out IEntity>): Int =
        added[entityType] ?: 0

    fun totalUpdated(entityType: KClass<out IEntity>): Int =
        updated[entityType] ?: 0

    fun totalDeleted(entityType: KClass<out IEntity>): Int =
        deleted[entityType] ?: 0

    fun totalUnchanged(entityType: KClass<out IEntity>): Int =
        unchanged[entityType] ?: 0

    fun totalDeletedShortcuts(): String =
        deletedShortcuts.toString()

}

