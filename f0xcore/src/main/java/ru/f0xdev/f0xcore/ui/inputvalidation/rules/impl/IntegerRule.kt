package ru.f0xdev.f0xcore.ui.inputvalidation.rules.impl

import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule

/**
 * Правило проверяет что значение является целым числом
 *
 * */
class IntegerRule(arg: String) : InputValidationRule(arg) {

    override fun validate(value: String, extraFieldValue: Any?): InputValidationError? {
        return try {
            value.toInt()
            null
        } catch (e: NumberFormatException) {
            return InputValidationError(R.string.validation_error_integer)
        }
    }
}