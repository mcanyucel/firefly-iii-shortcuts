package com.mustafacanyucel.fireflyiiishortcuts.services.sync

data class EntityLists<T>(
    val localList: List<T>,
    val remoteList: List<T>
)
