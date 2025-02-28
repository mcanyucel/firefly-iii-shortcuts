package com.mustafacanyucel.fireflyiiishortcuts.services.sync

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.IEntity
import com.mustafacanyucel.fireflyiiishortcuts.model.ComparisonResult

class EntityComparer {

    companion object {
        fun <T : IEntity> compareEntities(
            remoteEntities: List<T>,
            localEntities: List<T>
        ): ComparisonResult<T> {
            val localEntitiesMap = localEntities.associateBy { it.id }
            val remoteEntitiesMap = remoteEntities.associateBy { it.id }

            val added = remoteEntities.filter { !localEntitiesMap.containsKey(it.id) }
            val removed = localEntities.filter { !remoteEntitiesMap.containsKey(it.id) }

            val unchanged = mutableListOf<T>()
            val updated = mutableListOf<T>()

            remoteEntitiesMap.keys.intersect(localEntitiesMap.keys).forEach { id ->
                val remoteEntity = remoteEntitiesMap[id]!!
                val localEntity = localEntitiesMap[id]!!

                // this will use data class's equals method, comparing all properties
                if (remoteEntity == localEntity) {
                    unchanged.add(remoteEntity)
                } else {
                    updated.add(remoteEntity)
                }
            }
            return ComparisonResult(unchanged, added, removed, updated)
        }
    }
}