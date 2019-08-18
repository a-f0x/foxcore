package ru.f0xdev.f0xcore.presentation.presenters

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import ru.f0xdev.f0xcore.analytic.AnalyticEvent
import ru.f0xdev.f0xcore.analytic.IAnalytic
import ru.f0xdev.f0xcore.util.logError

abstract class ABaseAnalyticPresenter<View : MvpView>(private val analytic: IAnalytic? = null) : MvpPresenter<View>() {

    fun sendAnalytic(eventName: String, payload: Map<String, Any>?) {
        analytic?.sendAnalytics(AnalyticEvent(eventName, payload))
            ?: this.logError(
                "Perhaps you forgot to add the analyst to the base class constructor ${this::class.java.name}",
                null
            )
    }

}