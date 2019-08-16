package ru.f0xdev.f0xcore.ui.inputvalidation.rules.impl

import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule

/**
 * Правило валидации которое определяет что значение поля это целое число
 * и оно находится в допустимых пределах
 * **/
class BetweenRule(arg: String) : InputValidationRule(arg) {

    override fun validate(value: String, extraFieldValue: Any?): InputValidationError? {
        val split = arg.split(",")
        if (split.size < 2) {
            return null
        }

        return try {
            val start = split[0].toInt()
            val end = split[1].toInt()
            val int = value.toInt()
            if (int in start..end) {
                null
            } else {
                InputValidationError(
                    R.string.validation_error_between,
                    arrayOf(start.toString(), end.toString())
                )
            }
        } catch (e: Exception) {
            InputValidationError((R.string.validation_error_between_wrong_format))
        }
    }

}