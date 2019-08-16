package ru.f0xdev.f0xcore.ui.inputvalidation

import androidx.annotation.StringRes

class InputValidationError(
    @StringRes val messageId: Int = 0, var args: Array<String> = emptyArray(),
    val message: String = ""
)