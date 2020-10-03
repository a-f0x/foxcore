package ru.f0xdev.f0xcore.base.navigation

import ru.terrakok.cicerone.Navigator

interface NavigationLifeCycle {
    fun onAttachNavigator(navigator: Navigator)
    fun detachNavigator()
}