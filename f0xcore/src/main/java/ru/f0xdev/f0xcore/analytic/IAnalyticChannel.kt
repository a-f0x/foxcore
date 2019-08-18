package ru.f0xdev.f0xcore.analytic

interface IAnalyticChannel {
    fun sendEvent(analyticEvent: AnalyticEvent)
}