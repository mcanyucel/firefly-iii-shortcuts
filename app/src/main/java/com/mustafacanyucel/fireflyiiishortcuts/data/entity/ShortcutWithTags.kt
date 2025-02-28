package com.mustafacanyucel.fireflyiiishortcuts.data.entity

data class ShortcutWithTags(
    val shortcut: ShortcutEntity,
    val tags: List<TagEntity>
)
