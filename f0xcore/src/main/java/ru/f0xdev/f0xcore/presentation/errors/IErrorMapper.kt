package ru.f0xdev.f0xcore.presentation.errors


interface IErrorMapper {
    fun mapThrowableToError(throwable: Throwable): IError
}

