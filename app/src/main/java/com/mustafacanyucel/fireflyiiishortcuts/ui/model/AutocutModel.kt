package com.mustafacanyucel.fireflyiiishortcuts.ui.model

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutEntity
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AutocutFilterEntity

data class AutocutModel(
    val id: Long,
    val name: String,
    val template: String,
    val icon: String?,
    val autocutFilterEntities: List<AutocutFilterEntity>? = null
) {
    companion object {
        fun fromEntity(
            autocutEntity: AutocutEntity,
            autocutFilterEntities: List<AutocutFilterEntity>?
        ): AutocutModel {
            return AutocutModel(
                id = autocutEntity.id,
                name = autocutEntity.name,
                template = autocutEntity.template,
                icon = autocutEntity.icon,
                autocutFilterEntities = autocutFilterEntities
            )
        }

        fun createNew(): AutocutModel {
            return AutocutModel(
                id = 0L,
                name = "",
                template = "",
                icon = null,
                autocutFilterEntities = emptyList()
            )
        }
    }
}