package ru.f0xdev.f0xcore.base.navigation

import ru.f0xdev.f0xcore.base.navigation.routing.IRouter


/**
 * Используется в качестве рутового навигатора фрагмента,
 * то есть фрагмент внутри себя менеджит другие фрагменты.
 * никак не влияет на навигацию [IRouter]
 *
 * */

interface INavigationLifeCycleRouter : IRouter, NavigationLifeCycle
