package ru.f0xdev.f0xcore.analytic

import ru.f0xdev.f0xcore.util.logInfo

class AnalyticChannelLog : IAnalyticChannel {
    override fun sendEvent(analyticEvent: AnalyticEvent) {
        this.logInfo("Send event: $analyticEvent")
    }
}