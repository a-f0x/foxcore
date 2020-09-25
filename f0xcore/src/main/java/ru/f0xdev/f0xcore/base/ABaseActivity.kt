package ru.f0xdev.f0xcore.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatActivity
import ru.f0xdev.f0xcore.BuildConfig
import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.util.getValidatableViews
import ru.f0xdev.f0xcore.util.hideKeyboard
import ru.f0xdev.f0xcore.util.visible

abstract class ABaseActivity : MvpAppCompatActivity(), BaseView, BackPressable {

    private var rootView: View? = null

    override var listener: OnBackPressListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!BuildConfig.DEBUG) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0f
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        rootView = window.decorView.findViewById(android.R.id.content)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    open fun progressLayout(): View? = findViewById(R.id.progressLayout)

    override fun showProgress(show: Boolean) {
        progressLayout()?.let {
            if (show)
                hideKeyboard()
            it.visible(show)
        }
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
        }

        Toast.makeText(this, R.string.network_error_text, Toast.LENGTH_SHORT).show()
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
        }
        Toast.makeText(this, R.string.unknown_error_text, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(@StringRes messageId: Int) {
        showMessage(this.getString(messageId))
    }

    override fun showMessageWithAction(message: String, actionText: String, listener: View.OnClickListener) {
        hideKeyboard()
        rootView?.let {
            val snackBar = Snackbar.make(it, message, Snackbar.LENGTH_LONG).apply {
                setAction(actionText, listener)
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.core_primary_color))
                setActionTextColor(ContextCompat.getColor(context, R.color.core_button_text_color))
            }
            snackBar.show()
        } ?: Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessageWithAction(messageId: Int, actionTextId: Int, listener: View.OnClickListener) {
        showMessageWithAction(getString(messageId), getString(actionTextId), listener)
    }

    override fun showErrorWithRetryAndCustomText(action: () -> Unit, @StringRes messageId: Int, buttonText: Int) {
        errorView()?.let {
            it.setErrorText(messageId)
            it.setButtonRetryText(buttonText)
            it.onRetryAction(action)
            it.visible(true)
        }
    }

    override fun showErrorWithRetryAndCustomText(action: () -> Unit, messageText: String, @StringRes buttonText: Int) {
        errorView()?.let {
            it.setErrorText(messageText)
            it.setButtonRetryText(buttonText)
            it.onRetryAction(action)
            it.visible(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }

    override fun onBackPressed() {
        listener?.let {
            if (!it.onBackButtonPressed()) {
                return
            }
        }
        super.onBackPressed()
    }

    override fun showValidationError(details: Map<String, List<String>>) {
        getValidatableViews().forEach { vi ->
            val fKey = vi.fieldKey
            if (fKey != null)
                details[fKey]?.let { errors ->
                    vi.setError(errors[0])
                }
        }
    }

}
