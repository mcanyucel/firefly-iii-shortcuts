package com.mustafacanyucel.fireflyiiishortcuts.ui.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

import java.math.BigDecimal

object BigDecimalParceler : Parceler<BigDecimal> {
    override fun create(parcel: Parcel): BigDecimal {
        return BigDecimal(parcel.readString() ?: "0")
    }

    override fun BigDecimal.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this.toPlainString())
    }

}

@Parcelize
@TypeParceler<BigDecimal, BigDecimalParceler>()
data class ShortcutExecutionData(
    val id: Long,
    val name: String,
    val amount: BigDecimal,
    val fromAccountId: String?,
    val fromAccountName: String?,
    val toAccountId: String?,
    val toAccountName: String?,
    val categoryId: String?,
    val categoryName: String?,
    val description: String?,
    val tagIds: List<String>
) : Parcelable {
    companion object {
        fun fromModel(model: ShortcutModel): ShortcutExecutionData {
            return ShortcutExecutionData(
                id = model.id,
                name = model.name,
                amount = model.amount,
                fromAccountId = model.fromAccountEntity?.id,
                fromAccountName = model.fromAccountEntity?.name,
                toAccountId = model.toAccountEntity?.id,
                toAccountName = model.toAccountEntity?.name,
                categoryId = model.categoryEntity?.id,
                categoryName = model.categoryEntity?.name,
                description = model.description,
                tagIds = model.tagEntities.map { it.id }
            )
        }
    }
}
