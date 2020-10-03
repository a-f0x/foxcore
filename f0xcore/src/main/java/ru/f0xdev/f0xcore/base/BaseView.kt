package ru.f0xdev.f0xcore.base

import android.view.View
import androidx.annotation.StringRes
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface BaseView : MvpView {

    fun showNetworkError(action: (() -> Unit)? = null)

    fun finish()

    fun showUnknownErrorMessage(action: (() -> Unit)? = null)

    fun showMessage(message: String)

    fun showMessage(@StringRes messageId: Int)

    fun showProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorWithRetryAndCustomText(
        action: () -> Unit,
        @StringRes titleId: Int,
        @StringRes messageId: Int,
        @StringRes buttonText: Int
    )


    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorWithRetryAndCustomText(
        action: () -> Unit,
        titleText: String,
        messageText: String,
        buttonText: String
    )


    fun showMessageWithAction(message: String, actionText: String, listener: View.OnClickListener)

    fun showMessageWithAction(
        @StringRes messageId: Int,
        @StringRes actionTextId: Int,
        listener: View.OnClickListener
    )

    fun showValidationError(details: Map<String, List<String>>)
}