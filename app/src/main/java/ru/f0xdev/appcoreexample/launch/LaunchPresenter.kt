package ru.f0xdev.appcoreexample.launch

import com.arellomobile.mvp.InjectViewState
import ru.f0xdev.f0xcore.auth.IAuthEventListener
import ru.f0xdev.f0xcore.auth.IAuthManager
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.presentation.presenters.ABaseCoroutinePresenter
import ru.f0xdev.f0xcore.providers.ICoroutineContextProvider

@InjectViewState
class LaunchPresenter(
    errorProcessor: IErrorProcessor,
    provider: ICoroutineContextProvider,
    private val authManager: IAuthManager
) : ABaseCoroutinePresenter<MainView>(errorProcessor, provider, null) {

    private val authListener = object : IAuthEventListener {
        override fun onAccessTokenUpdated() {}

        override fun onLogin() {
            viewState.showMainFragment()
        }

        override fun onLogout() {
            viewState.showAuthFragment()
        }
    }

    override fun onFirstViewAttach() {
        authManager.addListener(authListener)
        if (authManager.isAuthenticated())
            viewState.showMainFragment()
        else
            viewState.showAuthFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        authManager.removeListener(authListener)
    }
}