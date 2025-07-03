package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "autocut_filters",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["fromAccountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["toAccountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = BillEntity::class,
            parentColumns = ["id"],
            childColumns = ["billId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = PiggybankEntity::class,
            parentColumns = ["id"],
            childColumns = ["piggybankId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("fromAccountId"),
        Index("toAccountId"),
        Index("categoryId"),
        Index("budgetId"),
        Index("billId"),
        Index("piggybankId")
    ]
    )

data class AutocutFilterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val fromAccountMatch: String,
    val toAccountMatch: String,
    val fromAccountId: String,
    val toAccountId: String,
    val categoryId: String? = null,
    val budgetId: String? = null,
    val billId: String? = null,
    val piggybankId: String? = null,
    val icon: String? = null,
    val transactionType: TransactionType
)