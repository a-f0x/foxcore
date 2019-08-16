package ru.f0xdev.f0xcore.ui.inputvalidation.rules.impl

import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule

/**
 * если поле пустое то считается что оно валидное
 * а если хоть что то в нем есть то это значение должно быть провалидированно
 *
 * */
class RegexAllowEmptyRule(pattern: String) : InputValidationRule(pattern) {

    override fun validate(value: String, extraFieldValue: Any?): InputValidationError? {
        val regex = arg.trim("/".first())
        if (value.isNullOrEmpty())
            return null
        if (!regex.toRegex().matches(value)) {
            return InputValidationError(R.string.validation_error_regex, arrayOf())
        }
        return null
    }
}