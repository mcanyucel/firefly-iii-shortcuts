package com.mustafacanyucel.fireflyiiishortcuts.services.repository

import com.mustafacanyucel.fireflyiiishortcuts.model.api.budget.BudgetData
import kotlinx.coroutines.flow.Flow

interface IBudgetRepository {
    suspend fun getBudgets(): Flow<ApiResult<List<BudgetData>>>
}