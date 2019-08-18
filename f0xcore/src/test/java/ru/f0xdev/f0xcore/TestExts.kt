package ru.f0xdev.f0xcore

import java.lang.reflect.Field

@Throws(Exception::class)
fun Any.setFieldValue(field: Field, newValue: Any): Any {
    field.isAccessible = true
    val modifiersField = Field::class.java.getDeclaredField("modifiers")
    modifiersField.isAccessible = true
    field.set(this, newValue)
    return this
}