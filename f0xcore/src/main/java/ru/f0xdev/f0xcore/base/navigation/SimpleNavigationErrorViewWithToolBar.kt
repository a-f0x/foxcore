package ru.f0xdev.f0xcore.base.navigation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar
import ru.f0xdev.f0xcore.R

class SimpleNavigationErrorViewWithToolBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ANavigationErrorViewWithToolBar(context, attrs, defStyleAttr) {

    override val rView: View
        get() = View.inflate(context, R.layout.error_view_with_tool_bar, this)

    override val toolBar: Toolbar = rView.findViewById(R.id.errorToolBar)
}
