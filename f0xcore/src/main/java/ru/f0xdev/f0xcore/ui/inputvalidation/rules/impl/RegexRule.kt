package ru.f0xdev.f0xcore.ui.inputvalidation.rules.impl

import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule

/**
 * Правило проверяет что значение удовлетворяет регулярному выражению
 * */

class RegexRule(pattern: String) : InputValidationRule(pattern) {
    override fun validate(value: String, extraFieldValue: Any?): InputValidationError? {
        val regex = arg.trim("/".first())
        if (!regex.toRegex().matches(value)) {
            return InputValidationError(R.string.validation_error_regex, arrayOf())
        }
        return null
    }

}