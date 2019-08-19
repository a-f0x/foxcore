package ru.f0xdev.f0xcore.ui.inputvalidation.views

import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule

interface ValidatableInput {
    var fieldKey: String?
    val onValidationSuccess: (input: ValidatableInput) -> Unit
    val onValidationError: (input: ValidatableInput) -> Unit
    fun getValidationValue(): String
    val validationRules: List<InputValidationRule>
    val validationErrors: MutableList<InputValidationError>

    fun setError(errorText: CharSequence?)

    fun validate(): Boolean {
        val errors = validationErrors
        errors.clear()

        validationRules.forEach {
            it.validate(getValidationValue())?.let { err ->
                errors.add(err)
            }
        }

        val inputIsValid = isValid()
        if (inputIsValid) onValidationSuccess(this) else onValidationError(this)
        return inputIsValid
    }

    fun isValid(): Boolean = validationErrors.isEmpty()
}
