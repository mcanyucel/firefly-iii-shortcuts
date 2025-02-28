package com.mustafacanyucel.fireflyiiishortcuts.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shortcuts",
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
data class ShortcutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val amount: String, // Storing as String to preserve precision for currency
    val fromAccountId: String,
    val toAccountId: String,
    val categoryId: String? = null, // Optional: a shortcut may not have a category
    val budgetId: String? = null, // Optional: a shortcut may not have a budget
    val billId: String? = null, // Optional: a shortcut may not have a bill
    val piggybankId: String? = null, // Optional: a shortcut may not have a piggybank
    val icon: String? = null, // Optional: for UI display
    val description: String? = null,
    val lastUsed: Long? = null // Timestamp when the shortcut was last used
)