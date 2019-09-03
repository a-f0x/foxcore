package ru.f0xdev.f0xcore.ui

import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.f0xdev.f0xcore.R

/**
 * Первый айтем в списке всегда будет серого цвета
 * */
open class SimpleSpinnerWithPlaceHolderAdapter<T>(context: Context, data: Array<T>) :
    ArrayAdapter<T>(context, R.layout.simple_spinner_item, data) {
    override fun isEnabled(position: Int): Boolean {
        return position > 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = super.getDropDownView(position, convertView, parent)
        if (position == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                (v as TextView).setTextColor(
                    context.resources.getColor(
                        R.color.core_text_secondary_color,
                        context.theme
                    )
                )
            } else {
                (v as TextView).setTextColor(context.resources.getColor(R.color.core_text_secondary_color))
            }
        }
        return v
    }
}