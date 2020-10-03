package ru.f0xdev.f0xcore.base.navigation.routing

import android.content.Context
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * Обертка самая высокая для навигации в корне активити
 * */
class ActivityRouter(private val router: Router, context: Context) : ABaseRouterOnUI(context),
    IRouter {

    override fun navigateTo(screen: SupportAppScreen) {
        runOnUI { router.navigateTo(screen) }
    }

    override fun replaceScreen(screen: SupportAppScreen) {
        runOnUI { router.replaceScreen(screen) }
    }

    override fun rootScreen(screen: SupportAppScreen) {
        runOnUI { router.newRootScreen(screen) }
    }

    override fun back() {
        runOnUI { router.exit() }
    }

    override fun backTo(screen: SupportAppScreen) {
        runOnUI { router.backTo(screen) }
    }

    override fun newChain(screens: Array<Screen>) {
        runOnUI { router.newChain(*screens) }
    }

    override fun newRootChain(screens: Array<Screen>) {
        runOnUI { router.newRootChain(*screens) }
    }

    override fun finishChain() {
        runOnUI { router.finishChain() }
    }
}