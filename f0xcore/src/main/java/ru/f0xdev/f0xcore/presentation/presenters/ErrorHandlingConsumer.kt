package ru.f0xdev.f0xcore.presentation.presenters

import io.reactivex.functions.Consumer
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.presentation.errors.IError
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor

open class ErrorHandlingConsumer<V : BaseView>(
    private val errorProcessor: IErrorProcessor,
    private val view: V,
    private val onError: (error: IError) -> Unit,
    private val retryAction: (() -> Unit)? = null

) : Consumer<Throwable> {

    override fun accept(throwable: Throwable) {
        errorProcessor.processExceptionOnView(throwable, view, onError, retryAction)
    }
}
