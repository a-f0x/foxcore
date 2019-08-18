package ru.f0xdev.f0xcore.presentation.errors

class ErrorProcessor(override val mapper: IErrorMapper, override val viewDispatcher: IErrorViewDispatcher) :
    IErrorProcessor