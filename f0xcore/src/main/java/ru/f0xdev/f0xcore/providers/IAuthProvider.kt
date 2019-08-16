package ru.f0xdev.f0xcore.providers

interface IAuthProvider {

    /**
     * ключ доступа к методам апи не азкрытыми авторизацией
     * */
    val getApiKey: String

    /**
     * ид клиента
     * */
    val clientlientId: String

    /**
     * Проверка авторизации клиента
     * */
    val isAuthenticated: Boolean

    /**
     * Возвращает тип токена  например "Bearer"
     *
     * */
    val getAuthTokenType: String?

    /**
     * Возвращает токен в виде "super_secret_user_token"
     *
     * */
    val getToken: String?

}