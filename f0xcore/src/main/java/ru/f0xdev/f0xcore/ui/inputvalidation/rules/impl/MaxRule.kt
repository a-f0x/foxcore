package ru.f0xdev.f0xcore.ui.inputvalidation.rules.impl

import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule

/**
 * Правило проверяет что значение целое число и оно не выходит за границы максимального значения
 * */

class MaxRule(arg: String) : InputValidationRule(arg) {

    override fun validate(value: String, extraFieldValue: Any?): InputValidationError? {
        try {
            val max = arg.toInt()
            if (value.length > max) {
                return InputValidationError(R.string.validation_error_max, arrayOf(max.toString()))
            }

        } catch (e: NumberFormatException) {
            return InputValidationError(R.string.validation_error_integer)
        }
        return null
    }
}