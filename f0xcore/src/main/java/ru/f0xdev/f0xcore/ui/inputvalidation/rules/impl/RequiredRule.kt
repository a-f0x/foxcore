package ru.f0xdev.f0xcore.ui.inputvalidation.rules.impl

import android.text.TextUtils
import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule

/**
 * Правило проверяет что значение обязательно заполнено
 * */
class RequiredRule(arg: String) : InputValidationRule(arg) {

    override fun validate(value: String, extraFieldValue: Any?): InputValidationError? {
        if (TextUtils.isEmpty(value)) {
            return InputValidationError(R.string.validation_error_required, arrayOf())
        }
        return null
    }
}