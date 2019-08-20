package ru.f0xdev.f0xcore.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface IEmptyView {
    fun setText(@StringRes resId: Int? = null)
    fun setText(text: CharSequence? = null)
    fun setImage(@DrawableRes resId: Int? = null)
    fun setImage(drawable: Drawable)
    fun setVisible(visible: Boolean)

}


class TextEmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), IEmptyView {

    override fun setText(resId: Int?) {
        resId?.let {
            setText(it)
        }
    }

    override fun setImage(drawable: Drawable) {}
    override fun setImage(resId: Int?) {}

    override fun setVisible(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }
}

