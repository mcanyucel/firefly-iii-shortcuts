package com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.BillEntity

class BillSpinnerAdapter(
    context: Context,
    resource: Int,
    private val bills: List<BillEntity?>
) : ArrayAdapter<BillEntity?>(context, resource, bills) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = bills[position]?.name ?: "No Bill"

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = bills[position]?.name ?: "No Bill"

        return view
    }

    fun getBillAtPosition(position: Int): BillEntity? {
        return if (position in 0 until count) {
            bills[position]
        } else {
            null
        }
    }

    fun getPositionForBillId(billId: String?): Int {
        if (billId == null) return 0 // Position of the null item, if present

        return bills.indexOfFirst { it?.id == billId }
            .takeIf { it >= 0 } ?: 0
    }
}