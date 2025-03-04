package com.mustafacanyucel.fireflyiiishortcuts.ui.management.adapter

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import com.mustafacanyucel.fireflyiiishortcuts.data.entity.CategoryEntity

class CategorySpinnerAdapter(
    context: Context,
    resource: Int,
    private val categories: List<CategoryEntity?>
): ArrayAdapter<CategoryEntity?>(context, resource, categories) {
    override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
        val view = convertView ?: android.view.LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)

        val textView = view.findViewById<android.widget.TextView>(android.R.id.text1)
        textView.text = categories[position]?.name ?: "No Category"

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
        val view = convertView ?: android.view.LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)

        val textView = view.findViewById<android.widget.TextView>(android.R.id.text1)
        textView.text = categories[position]?.name ?: "No Category"

        return view
    }

    fun getCategoryAt(position: Int): CategoryEntity? {
        return if (position in 0 until count) {
            categories[position]
        } else {
            null
        }
    }

    fun getPositionForCategoryId(categoryId: String?): Int {
        return if (categoryId == null) {
            0
        }
        else {
            categories.indexOfFirst { it?.id == categoryId }
                .takeIf { it >=0 } ?: 0
        }

    }
}