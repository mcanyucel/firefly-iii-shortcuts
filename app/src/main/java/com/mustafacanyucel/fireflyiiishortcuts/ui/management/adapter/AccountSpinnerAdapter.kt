package com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.AccountEntity

class AccountSpinnerAdapter(
    context: Context, resource: Int, private val accounts: List<AccountEntity?>
) : ArrayAdapter<AccountEntity>(context, resource, accounts) {
    override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
        val view = convertView ?: android.view.LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)

        val textView = view.findViewById<android.widget.TextView>(android.R.id.text1)
        textView.text = accounts[position]?.name

        return view
    }

    override fun getDropDownView(
        position: Int, convertView: View?, parent: android.view.ViewGroup
    ): View {
        val view = convertView ?: android.view.LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)

        val textView = view.findViewById<android.widget.TextView>(android.R.id.text1)
        textView.text = accounts[position]?.name

        return view
    }

    fun getAccountAt(position: Int): AccountEntity? {
        return if (position in 0 until count) {
            accounts[position]
        } else {
            null
        }
    }

    fun getPositionForAccountId(accountId: String?): Int {
        return if (accountId == null) {
            0
        } else {
            accounts.indexOfFirst { it?.id == accountId }.takeIf { it >= 0 } ?: 0
        }
    }
}