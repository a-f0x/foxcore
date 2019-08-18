package ru.f0xdev.f0xcore.analytic

open class AnalyticEvent(open val eventName: String, open val payload: Map<String, Any>?) {
    override fun toString(): String {
        return "AnalyticEvent(eventName='$eventName', payload=$payload)"
    }
}