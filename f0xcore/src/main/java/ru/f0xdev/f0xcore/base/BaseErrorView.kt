package ru.f0xdev.f0xcore.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.util.visible

abstract class BaseErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), IErrorView {

    abstract val rView: View
    private var titleView: TextView = rView.findViewById(R.id.tvErrorTitle)
    private var textView: TextView = rView.findViewById(R.id.text)
    private var btnRetry: Button = rView.findViewById(R.id.btnRetry)

    override fun setErrorText(text: String) {
        textView.text = text
    }

    override fun setErrorText(textId: Int) {
        textView.setText(textId)
    }

    override fun setErrorTitle(textId: Int) {
        titleView.setText(textId)
    }

    override fun setErrorTitle(text: String) {
        titleView.text = text
    }

    override fun onRetryAction(action: () -> Unit) {
        btnRetry.setOnClickListener {
            rView.visible(false)
            action()
        }
    }

    override fun setButtonRetryText(text: String) {
        btnRetry.text = text
    }

    override fun setButtonRetryText(textId: Int) {
        btnRetry.setText(textId)
    }

    override fun visible(visible: Boolean) {
        rView.visible(visible)
    }
}
