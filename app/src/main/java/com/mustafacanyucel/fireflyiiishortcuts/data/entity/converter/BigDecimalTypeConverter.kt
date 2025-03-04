package com.mustafacanyucel.fireflyiiishortcuts.data.entity.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalTypeConverter {
    @TypeConverter
    fun fromString(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    @TypeConverter
    fun toString(value: BigDecimal?): String? {
        return value?.toPlainString() // prevent scientific notation
    }
}