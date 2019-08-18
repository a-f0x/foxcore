package ru.f0xdev.f0xcore.presentation.errors

interface IError {
    var rawException: Throwable
    val type: String
    val details: Map<String, List<String>>
}