package ru.f0xdev.f0xcore.presentation.errors

import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.presentation.errors.ErrorConsts.NETWORK_ERROR
import ru.f0xdev.f0xcore.presentation.errors.ErrorConsts.UNKNOWN_ERROR
import ru.f0xdev.f0xcore.presentation.errors.ErrorConsts.VALIDATION_ERROR
import ru.f0xdev.f0xcore.ui.inputvalidation.views.ValidatableView

interface IErrorViewDispatcher {
    fun <V : BaseView> dispatchError(
        error: IError,
        view: V,
        onError: (error: IError) -> Unit,
        retryAction: (() -> Unit)? = null
    )

    //Error callbacks
    fun <V : BaseView> onValidationError(error: IError, view: V)

    fun <V : BaseView> onUnknownError(throwable: Throwable, view: V, retryAction: (() -> Unit)? = null)
    fun <V : BaseView> onNetworkError(view: V, retryAction: (() -> Unit)? = null)
}

open class ErrorViewDispatcher : IErrorViewDispatcher {

    override fun <V : BaseView> dispatchError(
        error: IError,
        view: V,
        onError: (error: IError) -> Unit,
        retryAction: (() -> Unit)?
    ) {
        when (error.type) {
            NETWORK_ERROR -> {
                onNetworkError(view, retryAction)
            }
            UNKNOWN_ERROR -> {
                onUnknownError(error.rawException, view, retryAction)
            }
            VALIDATION_ERROR -> {
                onValidationError(error, view)
            }
            else -> {
                onUnknownError(error.rawException, view)
            }
        }
        onError.invoke(error)
    }

    override fun <V : BaseView> onValidationError(error: IError, view: V) {
        view.showProgress(false)
        if (view is ValidatableView) {
            view.showValidationError(error.details)
        }
    }


    override fun <V : BaseView> onUnknownError(throwable: Throwable, view: V, retryAction: (() -> Unit)?) {
        view.showProgress(false)
        view.showUnknownErrorMessage(retryAction)
    }

    override fun <V : BaseView> onNetworkError(view: V, retryAction: (() -> Unit)?) {
        view.showProgress(false)
        view.showNetworkError(retryAction)
    }
}