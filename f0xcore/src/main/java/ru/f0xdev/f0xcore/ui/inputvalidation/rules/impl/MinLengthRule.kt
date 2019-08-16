package ru.f0xdev.f0xcore.ui.inputvalidation.rules.impl

import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule

/**
 *
 * Правило проверяет минимальную длину строки
 *
 * */
class MinLengthRule(arg: String) : InputValidationRule(arg) {

    override fun validate(value: String, extraFieldValue: Any?): InputValidationError? {
        if (value.isEmpty())
            return null

        return try {
            val len = arg.toInt()
            if (value.length != len) {
                InputValidationError(R.string.validation_error_digits_wrong_length, arrayOf(arg))
            } else
                null
        } catch (e: NumberFormatException) {
            InputValidationError(R.string.validation_error_digits)
        }
    }
}