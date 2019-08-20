package ru.f0xdev.appcoreexample.errors

import ru.f0xdev.f0xcore.presentation.errors.IError


data class Error(
    override val type: String,
    override val details: Map<String, List<String>>,
    override var rawException: Throwable
) : IError