package ru.f0xdev.f0xcore.ui.inputvalidation.rules.impl

import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule

/**
 * Правило проверяет что значение целое число и оно больше минимального значения
 * */

class MinRule(arg: String) : InputValidationRule(arg) {

    override fun validate(value: String, extraFieldValue: Any?): InputValidationError? {
        try {
            val min = arg.toInt()
            if (value.length < min) {
                return InputValidationError(R.string.validation_error_min, arrayOf(min.toString()))
            }

        } catch (e: Exception) {
            return InputValidationError(R.string.validation_error_integer)
        }
        return null
    }
}