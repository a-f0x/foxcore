package ru.f0xdev.f0xcore.base.navigation.routing

import android.content.Context
import ru.f0xdev.f0xcore.base.navigation.INavigationLifeCycleRouter
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * Используется в качестве рутового навигатора фрагмента,
 * то есть фрагмент внутри себя менеджит другие фрагменты.
 * */
class FragmentRouter(
    private val localNavigatorHolder: LocalCiceroneHolder,
    private val tag: String,
    context: Context
) : ABaseRouterOnUI(context), INavigationLifeCycleRouter {

    override fun onAttachNavigator(navigator: Navigator) {
        getCicerone(tag).navigatorHolder.setNavigator(navigator)
    }

    override fun detachNavigator() {
        getCicerone(tag).navigatorHolder.removeNavigator()
    }

    override fun navigateTo(screen: SupportAppScreen) {
        getCicerone(tag).router.also {
            runOnUI { it.navigateTo(screen) }
        }
    }

    override fun replaceScreen(screen: SupportAppScreen) {
        getCicerone(tag).router.also {
            runOnUI { it.replaceScreen(screen) }
        }
    }

    override fun rootScreen(screen: SupportAppScreen) {
        getCicerone(tag).router.also {
            runOnUI { it.newRootScreen(screen) }
        }
    }

    override fun back() {
        getCicerone(tag).router.also {
            runOnUI { it.exit() }
        }
    }

    override fun backTo(screen: SupportAppScreen) {
        getCicerone(tag).router.also {
            runOnUI { it.backTo(screen) }
        }
    }

    override fun newChain(screens: Array<Screen>) {
        getCicerone(tag).router.also {
            runOnUI { it.newChain(*screens) }
        }
    }

    override fun newRootChain(screens: Array<Screen>) {
        getCicerone(tag).router.also {
            runOnUI { it.newRootChain(*screens) }
        }
    }

    override fun finishChain() {
        getCicerone(tag).router.also {
            runOnUI { it.finishChain() }
        }
    }

    private fun getCicerone(tag: String): Cicerone<Router> {
        return localNavigatorHolder.getCicerone(tag)
    }

}