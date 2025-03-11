package com.mustafacanyucel.fireflyiiishortcuts.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import com.mustafacanyucel.fireflyiiishortcuts.data.AppDatabase
import com.mustafacanyucel.fireflyiiishortcuts.services.firefly.ShortcutExecutionService
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutExecutionData
import com.mustafacanyucel.fireflyiiishortcuts.ui.model.ShortcutState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShortcutWidgetExecutor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: AppDatabase,
    private val widgetRepository: ShortcutWidgetRepository
) {
    companion object {
        private const val TAG = "ShortcutWidgetExecutor"

        /**
         * Static helper method to start widget shortcut execution
         */
        fun executeShortcut(context: Context, shortcutId: Long) {
            val intent = Intent(context, ShortcutWidgetExecutionService::class.java)
            intent.putExtra(ShortcutWidgetProvider.EXTRA_SHORTCUT_ID, shortcutId)
            context.startService(intent)
        }
    }

    /**
     * Execute a shortcut with the given id
     */
    fun executeShortcut(shortcutId: Long) {
        Log.d(TAG, "Executing shortcut with id $shortcutId")

        widgetRepository.updateShortcutState(shortcutId, ShortcutState.QUEUED)
        ShortcutWidgetProvider.notifyShortcutStateChanged(context, shortcutId, ShortcutState.QUEUED)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val shortcutWithTags = database.shortcutDao().getShortcutWithTags(shortcutId)

                if (shortcutWithTags == null) {
                    Log.e(TAG, "Shortcut with id $shortcutId not found")
                    widgetRepository.updateShortcutState(shortcutId, ShortcutState.FAILURE)
                    ShortcutWidgetProvider.notifyShortcutStateChanged(
                        context,
                        shortcutId,
                        ShortcutState.FAILURE
                    )
                    stopWidgetService()
                    return@launch
                }

                val fromAccount =
                    database.accountDao().getAccountById(shortcutWithTags.shortcut.fromAccountId)
                val toAccount =
                    database.accountDao().getAccountById(shortcutWithTags.shortcut.toAccountId)
                val category = shortcutWithTags.shortcut.categoryId?.let {
                    database.categoryDao().getCategoryById(it)
                }
                val budget = shortcutWithTags.shortcut.budgetId?.let {
                    database.budgetDao().getBudgetById(it)
                }
                val bill =
                    shortcutWithTags.shortcut.billId?.let { database.billDao().getBillById(it) }
                val piggybank = shortcutWithTags.shortcut.piggybankId?.let {
                    database.piggybankDao().getPiggybankById(it)
                }

                val executionData = ShortcutExecutionData(
                    id = shortcutWithTags.shortcut.id,
                    name = shortcutWithTags.shortcut.name,
                    amount = shortcutWithTags.shortcut.amount,
                    transactionType = shortcutWithTags.shortcut.transactionType.toString(),
                    fromAccountId = fromAccount?.id,
                    fromAccountName = fromAccount?.name,
                    toAccountId = toAccount?.id,
                    toAccountName = toAccount?.name,
                    categoryId = category?.id,
                    categoryName = category?.name,
                    budgetId = budget?.id,
                    budgetName = budget?.name,
                    billId = bill?.id,
                    billName = bill?.name,
                    piggyBankId = piggybank?.id,
                    piggyBankName = piggybank?.name,
                    description = shortcutWithTags.shortcut.description,
                    currencyCode = fromAccount?.currencyCode,
                    tagNames = shortcutWithTags.tags.map { it.tag }
                )

                widgetRepository.updateShortcutState(shortcutId, ShortcutState.RUNNING)
                ShortcutWidgetProvider.notifyShortcutStateChanged(
                    context,
                    shortcutId,
                    ShortcutState.RUNNING
                )

                val intent = Intent(context, ShortcutExecutionService::class.java)
                intent.putExtra(ShortcutExecutionService.EXTRA_SHORTCUT_DATA, executionData)
                context.startForegroundService(intent)

                // TODO implement broadcast receiver to receive execution result
                Thread.sleep(2000)
                withContext(Dispatchers.IO) {
                    database.shortcutDao()
                        .updateShortcutLastUsed(shortcutId, System.currentTimeMillis())
                }

                widgetRepository.updateShortcutState(shortcutId, ShortcutState.SUCCESS)
                ShortcutWidgetProvider.notifyShortcutStateChanged(
                    context,
                    shortcutId,
                    ShortcutState.SUCCESS
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error executing shortcut with id $shortcutId", e)
                widgetRepository.updateShortcutState(shortcutId, ShortcutState.FAILURE)
                ShortcutWidgetProvider.notifyShortcutStateChanged(
                    context,
                    shortcutId,
                    ShortcutState.FAILURE
                )
            } finally {
                stopWidgetService()
            }
        }
    }

    /**
     * Sends a broadcast to the service to stop itself
     */
    private fun stopWidgetService() {
        val stopIntent = Intent(context, ShortcutWidgetExecutionService::class.java)
        stopIntent.action = ShortcutWidgetExecutionService.ACTION_STOP_SERVICE
        context.startService(stopIntent)
    }
}