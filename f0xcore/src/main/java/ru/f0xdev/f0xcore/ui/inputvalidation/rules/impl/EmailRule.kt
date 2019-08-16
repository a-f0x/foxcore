package ru.f0xdev.f0xcore.ui.inputvalidation.rules.impl

import android.util.Patterns
import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule

/***
 * Правило проверки валидности адреса электронной почты
 * */

class EmailRule(arg: String) : InputValidationRule(arg) {

    override fun validate(value: String, extraFieldValue: Any?): InputValidationError? {
        return if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) null
        else InputValidationError(R.string.validation_error_email)
    }
}