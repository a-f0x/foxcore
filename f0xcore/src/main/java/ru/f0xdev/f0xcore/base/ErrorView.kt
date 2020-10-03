package ru.f0xdev.f0xcore.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import ru.f0xdev.f0xcore.R

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseErrorView(context, attrs, defStyleAttr), IErrorView {
    override val rView: View
        get() = View.inflate(context, R.layout.error_view, this)


}