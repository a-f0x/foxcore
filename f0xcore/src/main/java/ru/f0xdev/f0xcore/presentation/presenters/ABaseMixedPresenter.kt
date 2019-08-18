package ru.f0xdev.f0xcore.presentation.presenters

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import ru.f0xdev.f0xcore.analytic.IAnalytic
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.providers.ICoroutineContextProvider
import ru.f0xdev.f0xcore.providers.IRxSchedulerProvider

open class ABaseMixedPresenter<View : BaseView>(
    override val errorProcessor: IErrorProcessor,
    override val schedulerProvider: IRxSchedulerProvider,
    override val provider: ICoroutineContextProvider,
    analytic: IAnalytic? = null
) : ABaseAnalyticPresenter<View>(analytic), RxUIExecutor<View>,
    CoroutineUIExecutor<View> {

    override val view: View = viewState

    override val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override val compositeJob: CompositeJob = CompositeJob()

    override fun release() {
        compositeJob.cancel()
        compositeDisposable.dispose()
    }

    @CallSuper
    override fun onDestroy() {
        release()
    }
}