package ru.f0xdev.appcoreexample.auth

import com.arellomobile.mvp.InjectViewState
import ru.f0xdev.f0xcore.auth.IAuthManager
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.presentation.presenters.ABaseCoroutinePresenter
import ru.f0xdev.f0xcore.providers.ICoroutineContextProvider


@InjectViewState
class AuthPresenter(
    errorProcessor: IErrorProcessor,
    provider: ICoroutineContextProvider,
    private val authManager: IAuthManager
) : ABaseCoroutinePresenter<AuthView>(errorProcessor, provider, null) {


    fun login(email: String, password: String) {
        launchOnUI(
            block = {
                launchBackground { authManager.login(email, password) }.await()
                viewState.showProgress(false)
            }
        )
    }

}