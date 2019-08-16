package ru.f0xdev.f0xcore.ui.inputvalidation.views

import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule

interface ValidatableInput {
    var fieldKey: String?
    val onValidationSuccess: (input: ValidatableInput) -> Unit
    val onValidationError: (input: ValidatableInput) -> Unit
    val validationInput: String
    val validationRules: List<InputValidationRule>
    val validationErrors: MutableList<InputValidationError>

    fun validate(): Boolean {
        val errors = validationErrors
        errors.clear()

        validationRules.forEach {
            it.validate(validationInput)?.let { err ->
                errors.add(err)
            }
        }

        val inputIsValid = isValid()
        if (inputIsValid) onValidationSuccess(this) else onValidationError(this)
        return inputIsValid
    }

    fun isValid(): Boolean = validationErrors.isEmpty()
}
