package ru.f0xdev.appcoreexample.launch

import moxy.InjectViewState
import ru.f0xdev.appcoreexample.routing.IAppRouter
import ru.f0xdev.f0xcore.auth.IAuthEventListener
import ru.f0xdev.f0xcore.auth.IAuthManager
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.presentation.presenters.ABaseCoroutinePresenter
import ru.f0xdev.f0xcore.providers.ICoroutineContextProvider

@InjectViewState
class LaunchPresenter(
    errorProcessor: IErrorProcessor,
    provider: ICoroutineContextProvider,
    private val authManager: IAuthManager,
    private val router: IAppRouter
) : ABaseCoroutinePresenter<BaseView>(errorProcessor, provider, null) {

    private val authListener = object : IAuthEventListener {
        override fun onAccessTokenUpdated() {}

        override fun onLogin() {
            router.showMainScreen()
        }

        override fun onLogout() {
            router.showAuthScreen()

        }
    }

    override fun onFirstViewAttach() {
        authManager.addListener(authListener)
        if (authManager.isAuthenticated())
            router.showMainScreen()
        else
            router.showAuthScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        authManager.removeListener(authListener)
    }
}