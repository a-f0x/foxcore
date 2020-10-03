package ru.f0xdev.appcoreexample.routing

import ru.f0xdev.f0xcore.base.navigation.routing.IRouter

class AppRouter(private val activityRouter: IRouter) : IAppRouter {

    override fun showAuthScreen() {
        activityRouter.replaceScreen(Screens.AuthScreen)
    }

    override fun showMainScreen() {
        activityRouter.replaceScreen(Screens.MainScreen)
    }

}