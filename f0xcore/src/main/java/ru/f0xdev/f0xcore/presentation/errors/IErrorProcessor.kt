package ru.f0xdev.f0xcore.presentation.errors

import ru.f0xdev.f0xcore.base.BaseView

interface IErrorProcessor {
    val mapper: IErrorMapper
    val viewDispatcher: IErrorViewDispatcher

    fun <V : BaseView> processExceptionOnView(
        throwable: Throwable,
        view: V,
        onError: (error: IError) -> Unit,
        retryAction: (() -> Unit)? = null
    ) {
        throwable.printStackTrace()
        val e = mapper.mapThrowableToError(throwable)
        viewDispatcher.dispatchError(e, view, onError, retryAction)
    }

}