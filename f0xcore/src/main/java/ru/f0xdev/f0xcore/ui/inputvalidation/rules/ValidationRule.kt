package ru.f0xdev.f0xcore.ui.inputvalidation.rules

import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError


abstract class InputValidationRule(val arg: String) {

    /**
     * Проверяет удовлетворяет ли значение поля правилу.
     * @param value - значение которое надо проверить
     * @param extraFieldValue - Допустимое знаечения для value если необходимо
     * @return null если value удовлетворяет правилу, в противном случае возвращает объект  [InputValidationError]
     */
    abstract fun validate(value: String, extraFieldValue: Any? = null): InputValidationError?
}