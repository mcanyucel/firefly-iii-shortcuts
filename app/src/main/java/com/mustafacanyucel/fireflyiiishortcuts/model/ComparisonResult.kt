package com.mustafacanyucel.fireflyiiishortcuts.model

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.IEntity

data class ComparisonResult<T: IEntity>(
    val unchanged: List<T>,
    val added: List<T>,
    val removed: List<T>,
    val updated: List<T>,
)
