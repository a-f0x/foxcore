package ru.f0xdev.f0xcore.auth.net

import ru.f0xdev.f0xcore.auth.models.AccessToken
import ru.f0xdev.f0xcore.auth.models.AuthException


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



