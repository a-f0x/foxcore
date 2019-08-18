package ru.f0xdev.f0xcore.analytic

class Analytic(private val channels: List<IAnalyticChannel>) : IAnalytic {

    override fun sendAnalytics(analyticEvent: AnalyticEvent) {
        channels.forEach {
            it.sendEvent(analyticEvent)
        }
    }
}