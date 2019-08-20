package ru.f0xdev.f0xcore.auth.models

import com.google.gson.annotations.SerializedName

data class AuthException(
    @SerializedName("type") val type: Type,
    @SerializedName("mesage") override val message: String
) : Throwable(message) {
    enum class Type {

        /**
         * Ошибка сетевая
         * */
        NETWORK,

        /**
         * Ошибка сервера 500 и всякое такое
         * */
        UNKNOWN_ERROR,

        /**
         * Рефреш токен истек и надо разлогинить
         * */
        REFRESH_TOKEN_EXPIRED,

        /**
         * Не верные данные для авторизации
         * */
        WRONG_CREDENTIALS,
        /**
         * Способ авторизации не поддерживается
         * */
        AUTH_TYPE_NOT_SUPPORT
    }
}
