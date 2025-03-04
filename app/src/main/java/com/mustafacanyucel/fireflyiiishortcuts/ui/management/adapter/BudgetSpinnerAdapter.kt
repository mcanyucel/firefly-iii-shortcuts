package com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BudgetEntity

class BudgetSpinnerAdapter(
    context: Context,
    resource: Int,
    private val budgets: List<BudgetEntity?>
) : ArrayAdapter<BudgetEntity?>(context, resource, budgets) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = budgets[position]?.name ?: "No Budget"

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = budgets[position]?.name ?: "No Budget"

        return view
    }

    fun getBudgetAtPosition(position: Int): BudgetEntity? {
        return if (position in 0 until count) {
            budgets[position]
        } else {
            null
        }
    }

    fun getPositionForBudgetId(budgetId: String?): Int {
        if (budgetId == null) return 0 // Position of the null item, if present

        return budgets.indexOfFirst { it?.id == budgetId }
            .takeIf { it >= 0 } ?: 0
    }
}