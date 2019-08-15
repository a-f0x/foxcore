package ru.f0xdev.f0xcore.base

import androidx.annotation.StringRes

interface IErrorView {
    fun setErrorText(text: String)
    fun setErrorText(@StringRes textId: Int)
    fun setButtonRetryText(text: String)
    fun setButtonRetryText(@StringRes textId: Int)
    fun onRetryAction(action: () -> Unit)
    fun visible(visible: Boolean)
}