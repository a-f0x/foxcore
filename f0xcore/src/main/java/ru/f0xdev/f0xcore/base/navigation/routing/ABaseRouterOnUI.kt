package ru.f0xdev.f0xcore.base.navigation.routing

import android.content.Context
import android.os.Handler

abstract class ABaseRouterOnUI(private val context: Context) {

    protected fun runOnUI(block: () -> Unit) {
        Handler(context.mainLooper).post {
            block()
        }
    }
}