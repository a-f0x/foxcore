package ru.f0xdev.f0xcore.presentation.presenters

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import ru.f0xdev.f0xcore.analytic.IAnalytic
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.providers.IRxSchedulerProvider

open class ABaseRxPresenter<View : BaseView>(
    override val errorProcessor: IErrorProcessor,
    override val schedulerProvider: IRxSchedulerProvider,
    analytic: IAnalytic? = null

) : ABaseAnalyticPresenter<View>(analytic), RxUIExecutor<View> {

    override val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override val view: View = viewState

    @CallSuper
    override fun onDestroy() {
        release()
    }

}