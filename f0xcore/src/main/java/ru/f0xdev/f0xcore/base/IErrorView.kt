package ru.f0xdev.f0xcore.base

import androidx.annotation.StringRes

interface IErrorView {
    fun setErrorText(text: String)
    fun setErrorText(@StringRes textId: Int)
    fun setErrorTitle(@StringRes textId: Int)
    fun setErrorTitle(text: String)


    fun setButtonRetryText(text: String)
    fun setButtonRetryText(@StringRes textId: Int)
    fun onRetryAction(action: () -> Unit)
    fun visible(visible: Boolean)
}