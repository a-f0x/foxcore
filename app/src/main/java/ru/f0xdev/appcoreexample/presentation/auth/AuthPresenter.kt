package ru.f0xdev.appcoreexample.presentation.auth

import ru.f0xdev.f0xcore.auth.IAuthManager
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.presentation.presenters.ABaseCoroutinePresenter
import ru.f0xdev.f0xcore.providers.ICoroutineContextProvider

class AuthPresenter(
    errorProcessor: IErrorProcessor,
    provider: ICoroutineContextProvider,
    private val authManager: IAuthManager
) : ABaseCoroutinePresenter<AuthView>(errorProcessor, provider, null) {


    fun login(email: String, password: String) {
        launchOnUI(
            block = {
                launchBackground { authManager.login(email, password) }.await()

            }, onError = {

            })

    }

}