package ru.f0xdev.f0xcore.presentation.presenters

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.presentation.errors.ErrorConsts.VALIDATION_ERROR
import ru.f0xdev.f0xcore.presentation.errors.IError
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.providers.IRxSchedulerProvider

interface RxUIExecutor<View : BaseView> {
    val errorProcessor: IErrorProcessor
    val schedulerProvider: IRxSchedulerProvider
    val view: View
    val compositeDisposable: CompositeDisposable


    fun unSubscribeOnDestroy(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun release() {
        compositeDisposable.dispose()
    }

    fun <T> Single<T>.executeInIOAndObserveUI(
        successConsumer: Consumer<T> = Consumer {},
        errorConsumer: Consumer<Throwable> = Consumer {}
    ): Disposable {
        return subscribeOnAndObserveOn(schedulerProvider.io, schedulerProvider.ui, successConsumer, errorConsumer)
    }

    fun <T> Single<T>.subscribeOnAndObserveOn(
        subscribeOnScheduler: Scheduler?, observeOnScheduler: Scheduler?,
        successConsumer: Consumer<T> = Consumer {},
        errorConsumer: Consumer<Throwable> = Consumer {}
    ): Disposable {

        var source = this
        if (subscribeOnScheduler != null)
            source = source.subscribeOn(subscribeOnScheduler)
        if (observeOnScheduler != null)
            source = source.observeOn(observeOnScheduler)
        val disposable = source.subscribe(successConsumer, errorConsumer)
        addDisposable(disposable)
        return disposable
    }

    fun <T> Observable<T>.executeInIOAndObserveUI(
        onNextConsumer: Consumer<T> = Consumer {},
        onErrorConsumer: Consumer<Throwable> = Consumer {},
        onCompleteAction: Action = Action {}
    ): Disposable {
        return subscribeOnAndObserveOn(
            schedulerProvider.io,
            schedulerProvider.ui,
            onNextConsumer,
            onErrorConsumer,
            onCompleteAction
        )
    }


    fun <T> Observable<T>.subscribeOnAndObserveOn(
        subscribeOnScheduler: Scheduler?, observeOnScheduler: Scheduler?,
        onNextConsumer: Consumer<T> = Consumer {},
        onErrorConsumer: Consumer<Throwable> = Consumer {},
        onCompleteAction: Action = Action {}
    ): Disposable {
        var source = this
        if (subscribeOnScheduler != null)
            source = source.subscribeOn(subscribeOnScheduler)
        if (observeOnScheduler != null)
            source = source.observeOn(observeOnScheduler)
        val disposable = source.subscribe(
            onNextConsumer,
            onErrorConsumer,
            onCompleteAction
        )
        addDisposable(disposable)
        return disposable

    }

    fun Completable.executeInIOAndObserveUI(
        successAction: Action = Action {},
        errorConsumer: Consumer<Throwable> = Consumer {}
    ): Disposable {
        return subscribeOnAndObserveOn(schedulerProvider.io, schedulerProvider.ui, successAction, errorConsumer)
    }


    fun Completable.subscribeOnAndObserveOn(
        subscribeOnScheduler: Scheduler?, observeOnScheduler: Scheduler?,
        successAction: Action = Action {},
        errorConsumer: Consumer<Throwable> = Consumer {}
    ): Disposable {
        var source = this
        if (subscribeOnScheduler != null)
            source = source.subscribeOn(subscribeOnScheduler)
        if (observeOnScheduler != null)
            source = source.observeOn(observeOnScheduler)
        val disposable = source.subscribe(successAction, errorConsumer)
        addDisposable(disposable)
        return disposable
    }

    fun <T> Single<T>.subscribeByMy(
        showProgress: Boolean = true,
        onSuccess: (T) -> Unit = {},
        onValidationError: (IError) -> Unit = { },
        retryAction: (() -> Unit)? = null,
        onError: ((error: IError) -> Unit)? = null
    ): Disposable {

        val successConsumer = Consumer<T> { t -> onSuccess.invoke(t) }
        val onErrorCallback = mapErrorCallbacks(onValidationError, onError)

        val errorConsumer = ErrorHandlingConsumer(
            errorProcessor = errorProcessor,
            view = view,
            onError = onErrorCallback,
            retryAction = retryAction
        )

        if (showProgress)
            view.showProgress(true)
        return executeInIOAndObserveUI(successConsumer, errorConsumer)

    }

    fun Completable.subscribeByMy(
        showProgress: Boolean = true,
        onSuccess: () -> Unit = {},
        onValidationError: (IError) -> Unit = { },
        retryAction: (() -> Unit)? = null,
        onError: ((error: IError) -> Unit)? = null
    ): Disposable {

        val successAction = Action { onSuccess.invoke() }
        val onErrorCallback = mapErrorCallbacks(onValidationError, onError)

        val errorConsumer = ErrorHandlingConsumer(
            errorProcessor = errorProcessor,
            view = view,
            onError = onErrorCallback,
            retryAction = retryAction
        )

        if (showProgress)
            view.showProgress(true)
        return executeInIOAndObserveUI(successAction, errorConsumer)
    }


    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun mapErrorCallbacks(
        onValidationError: (IError) -> Unit = { },
        onError: ((error: IError) -> Unit)? = null
    ): (error: IError) -> Unit {

        return { error: IError ->
            when (error.type) {
                VALIDATION_ERROR -> {
                    onValidationError.invoke(error)
                }
            }
            onError?.invoke(error)
        }
    }
}