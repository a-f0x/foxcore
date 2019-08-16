package ru.f0xdev.f0xcore.ui.inputvalidation.rules.impl

import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule

/**
 *  Сравнивает булевые значения
 *  Используется для чекбоксов или радиогрупп
 *
 * */
class CheckedRule(arg: String) : InputValidationRule(arg) {

    override fun validate(value: String, extraFieldValue: Any?): InputValidationError? {
        return try {
            val isChecked = value.toBoolean()
            if (!isChecked) {
                InputValidationError(R.string.validation_error_checked)
            } else
                null
        } catch (e: Throwable) {
            InputValidationError(R.string.validation_error_checked)
        }
    }
}