package com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.PiggybankEntity

class PiggybankSpinnerAdapter(
    context: Context, resource: Int, private val piggybanks: List<PiggybankEntity?>
) : ArrayAdapter<PiggybankEntity?>(context, resource, piggybanks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = piggybanks[position]?.name ?: "No Piggybank"

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = piggybanks[position]?.name ?: "No Piggybank"

        return view
    }

    fun getPiggybankAtPosition(position: Int): PiggybankEntity? {
        return if (position in 0 until count) {
            piggybanks[position]
        } else {
            null
        }
    }

    fun getPositionForPiggybankId(piggybankId: String?): Int {
        if (piggybankId == null) return 0 // Position of the null item, if present

        return piggybanks.indexOfFirst { it?.id == piggybankId }
            .takeIf { it >= 0 } ?: 0
    }
}