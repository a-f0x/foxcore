package ru.f0xdev.f0xcore.presentation.presenters


import androidx.annotation.CallSuper
import ru.f0xdev.f0xcore.analytic.IAnalytic
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.providers.ICoroutineContextProvider

open class ABaseCoroutinePresenter<View : BaseView>(
    override val errorProcessor: IErrorProcessor,
    override val provider: ICoroutineContextProvider,
    analytic: IAnalytic? = null
) : ABaseAnalyticPresenter<View>(analytic), CoroutineUIExecutor<View> {

    override val view: View = viewState

    override val compositeJob: CompositeJob = CompositeJob()

    @CallSuper
    override fun onDestroy() {
        release()
    }
}


