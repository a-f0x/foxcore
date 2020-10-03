package ru.f0xdev.f0xcore.base.navigation

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import ru.f0xdev.f0xcore.base.BaseErrorView
import ru.f0xdev.f0xcore.base.IErrorView

abstract class ANavigationErrorViewWithToolBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseErrorView(context, attrs, defStyleAttr), IErrorView {

    abstract val toolBar: Toolbar
}