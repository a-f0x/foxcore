package ru.f0xdev.f0xcore.auth.net

import finance.robo.android.accountservice.models.AuthException
import ru.f0xdev.f0xcore.auth.models.AccessToken


/**
 * При реализации интерфейса авторизации пожалуйста обработайте все ошибки и пробросьте наверх только [AuthException]
 *
 * */
interface IAuthRemoteDataSource {
    @Throws(AuthException::class)
    fun auth(login: String, password: String): AccessToken

    @Throws(AuthException::class)
    fun refresh(refreshToken: String): AccessToken
}



