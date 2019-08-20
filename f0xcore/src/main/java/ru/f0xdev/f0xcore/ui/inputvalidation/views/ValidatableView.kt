package ru.f0xdev.f0xcore.ui.inputvalidation.views

import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType


@StateStrategyType(OneExecutionStateStrategy::class)
interface ValidatableView {

    fun validateInputs(
        inputs: List<ValidatableInput>,
        onSuccess: () -> Unit,
        onError: ((failedInputs: List<ValidatableInput>) -> Unit)? = null
    ) {
        var isValid = true
        val failedInputs = mutableListOf<ValidatableInput>()

        inputs.forEach {
            val inputIsValid = it.validate()
            if (!inputIsValid) {
                failedInputs.add(it)
            }
            isValid = isValid && inputIsValid
        }

        if (isValid) onSuccess() else onError?.invoke(failedInputs)
    }


}