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
    private var tvErrorTitle: TextView = rView.findViewById(R.id.tvErrorTitle)
    private var tvErrorMessage: TextView = rView.findViewById(R.id.tvErrorMessage)
    private var btnRetry: Button = rView.findViewById(R.id.btnRetry)

    override fun setErrorText(text: String) {
        tvErrorMessage.text = text
    }

    override fun setErrorText(textId: Int) {
        tvErrorMessage.setText(textId)
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

    override fun setErrorTitle(textId: Int) {
        tvErrorTitle.setText(textId)
    }

    override fun setErrorTitle(text: String) {
        tvErrorTitle.text = text
    }

}
