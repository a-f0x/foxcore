package ru.f0xdev.f0xcore.base.navigation

import ru.f0xdev.f0xcore.base.ABaseActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder

abstract class ABaseNavigationActivity : ABaseActivity() {
    abstract val navigatorHolder: NavigatorHolder

    abstract val navigator: Navigator


    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

}