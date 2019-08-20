package ru.f0xdev.f0xcore.auth

import ru.f0xdev.f0xcore.auth.models.AccessToken
import ru.f0xdev.f0xcore.auth.models.AuthException

interface IAuthManager {

    fun getToken(): String

    fun getApiKey(): String

    fun isAuthenticated(): Boolean

    @Throws(AuthException::class)
    fun login(username: String, password: String)

    fun logout()

    fun invalidateToken()

    /**
     * через эту функцию можно будет установить токен полученный из других источников,
     * например при авторизации через соцсети
     * */
    fun setAccessToken(accessToken: AccessToken)

    fun getAuthType(): String?

    fun addListener(listener: IAuthEventListener)

    fun removeListener(listener: IAuthEventListener)
}