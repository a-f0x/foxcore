package ru.f0xdev.f0xcore.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.base.mvp.MvpAppCompatFragment
import ru.f0xdev.f0xcore.util.hideKeyboard
import ru.f0xdev.f0xcore.util.visible

abstract class ABaseFragment : MvpAppCompatFragment(), BaseView {

    protected lateinit var fragmentContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentContext = context ?: throw IllegalStateException("Context is null for ${javaClass.canonicalName} $this")
        super.onCreate(savedInstanceState)
    }

    fun setTitle(titleId: Int) {
        activity?.setTitle(titleId)
    }

    fun setTitle(title: String) {
        activity?.title = title
    }

    open fun progressLayout(): View? = activity?.findViewById(R.id.progressLayout)

    override fun showProgress(show: Boolean) {
        progressLayout()?.let {
            if (show)
                hideKeyboard()
            it.visible(show)
        }
    }

    override fun finish() {
        activity?.finish()
    }

    open fun errorView(): IErrorView? = null

    override fun showNetworkError(action: (() -> Unit)?) {
        hideKeyboard()
        action?.let { a ->
            errorView()?.let { ev ->
                ev.setErrorText(R.string.network_error_text)
                ev.onRetryAction(a)
                ev.visible(true)
                return
            }
            activity?.let {
                if (it is ABaseActivity)
                    it.showNetworkError(action)
                return
            }
        }
        activity?.let {
            if (it is ABaseActivity)
                it.showNetworkError()
        }
    }

    override fun showUnknownErrorMessage(action: (() -> Unit)?) {
        hideKeyboard()
        action?.let { a ->
            errorView()?.let { ev ->
                ev.setErrorText(R.string.unknown_error_text)
                ev.onRetryAction(a)
                ev.visible(true)
                return
            }
            activity?.let {
                if (it is ABaseActivity)
                    it.showUnknownErrorMessage(action)
                return
            }
        }
        activity?.let {
            if (it is ABaseActivity)
                it.showUnknownErrorMessage()
        }
    }

    override fun showMessage(message: String) {
        hideKeyboard()
        activity?.let {
            if (it is ABaseActivity)
                it.showMessage(message)
        }
    }

    override fun showMessage(@StringRes messageId: Int) {
        hideKeyboard()
        showMessage(getString(messageId))
    }

    override fun showMessageWithAction(message: String, actionText: String, listener: View.OnClickListener) {
        hideKeyboard()
        activity?.let {
            if (it is ABaseActivity)
                it.showMessageWithAction(message, actionText, listener)
        }
    }

    override fun showMessageWithAction(messageId: Int, actionTextId: Int, listener: View.OnClickListener) {
        hideKeyboard()
        showMessageWithAction(getString(messageId), getString(actionTextId), listener)
    }

    override fun showErrorWithRetryAndCustomText(action: () -> Unit, messageId: Int, buttonText: Int) {
        hideKeyboard()
        errorView()?.let {
            it.setErrorText(messageId)
            it.setButtonRetryText(buttonText)
            it.onRetryAction(action)
            it.visible(true)
        }
    }

    override fun showErrorWithRetryAndCustomText(action: () -> Unit, messageText: String, @StringRes buttonText: Int) {
        hideKeyboard()
        errorView()?.let {
            it.setErrorText(messageText)
            it.setButtonRetryText(buttonText)
            it.onRetryAction(action)
            it.visible(true)
        }
    }

    private fun hideKeyboard() {
        activity?.hideKeyboard()
    }

}
