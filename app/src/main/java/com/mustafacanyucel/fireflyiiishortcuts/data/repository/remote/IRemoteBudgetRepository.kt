package com.mustafacanyucel.fireflyiiishortcuts.data.repository.remote

import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

interface IRemoteBudgetRepository {
    suspend fun getBudgets(): Flow<ApiResult<List<BudgetEntity>>>
}