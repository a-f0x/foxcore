package ru.f0xdev.f0xcore.base

import android.view.View
import androidx.annotation.StringRes
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface BaseView : MvpView {

    fun showNetworkError(action: (() -> Unit)? = null)

    fun finish()

    fun showUnknownErrorMessage(action: (() -> Unit)? = null)

    fun showMessage(message: String)

    fun showMessage(@StringRes messageId: Int)

    fun showProgress(show: Boolean)

    fun showErrorWithRetryAndCustomText(action: () -> Unit, @StringRes messageId: Int, @StringRes buttonText: Int)

    fun showErrorWithRetryAndCustomText(action: () -> Unit, messageText: String, @StringRes buttonText: Int)

    fun showMessageWithAction(message: String, actionText: String, listener: View.OnClickListener)

    fun showMessageWithAction(@StringRes messageId: Int, @StringRes actionTextId: Int, listener: View.OnClickListener)
}