package com.mustafacanyucel.fireflyiiishortcuts.ui.model

import android.os.Parcel
import android.os.Parcelable
import com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction.TransactionRequest
import com.mustafacanyucel.fireflyiiishortcuts.model.api.transaction.TransactionRequestItem
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import java.math.BigDecimal
import java.time.OffsetDateTime

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
    val transactionType: String,
    val fromAccountId: String?,
    val fromAccountName: String?,
    val toAccountId: String?,
    val toAccountName: String?,
    val categoryId: String?,
    val categoryName: String?,
    val budgetId: String?,
    val billId: String?,
    val billName: String?,
    val budgetName: String?,
    val piggyBankId: String?,
    val piggyBankName: String?,
    val tagNames: List<String>?,
    val currencyCode: String?,
    val description: String?,
) : Parcelable {

    fun toTransactionRequest(): TransactionRequest {
        val transactionItem = TransactionRequestItem(
            type = this.transactionType,
            dateUTCString = OffsetDateTime.now().toString(),
            amount = this.amount.toPlainString(),
            description = this.description ?: this.name,
            fromAccountId = this.fromAccountId!!,
            fromAccountName = this.fromAccountName!!,
            toAccountId = this.toAccountId!!,
            toAccountName = this.toAccountName!!,
            currencyCode = this.currencyCode,
            budgetId = this.budgetId,
            budgetName = this.budgetName,
            categoryId = this.categoryId,
            categoryName = this.categoryName,
            piggyBankId = this.piggyBankId,
            piggyBankName = this.piggyBankName,
            billId = this.billId,
            billName = this.billName,
            tags = this.tagNames
        )

        return TransactionRequest(
            transactions = listOf(transactionItem)
        )
    }

    companion object {
        fun fromModel(model: ShortcutModel): ShortcutExecutionData {
            return ShortcutExecutionData(
                id = model.id,
                name = model.name,
                amount = model.amount,
                transactionType = model.transactionType.toString(),
                fromAccountId = model.fromAccountEntity?.id,
                fromAccountName = model.fromAccountEntity?.name,
                toAccountId = model.toAccountEntity?.id,
                toAccountName = model.toAccountEntity?.name,
                categoryId = model.categoryEntity?.id,
                categoryName = model.categoryEntity?.name,
                description = model.description,
                budgetId = model.budgetEntity?.id,
                budgetName = model.budgetEntity?.name,
                piggyBankId = model.piggybankEntity?.id,
                piggyBankName = model.piggybankEntity?.name,
                currencyCode = model.fromAccountEntity?.currencyCode,
                billId = model.billEntity?.id,
                billName = model.billEntity?.name,
                tagNames = model.tagEntities.map { it.tag }
            )
        }
    }
}
