package ru.f0xdev.f0xcore.auth

import ru.f0xdev.f0xcore.util.Cryptographer


interface ITokenCryptographer {
    fun encryptToken(token: String, secretKey: String): String

    fun decryptToken(cipherText: String, secretKey: String): String

}

class TokenCryptographer(private val cryptographer: Cryptographer) : ITokenCryptographer {
    override fun encryptToken(token: String, secretKey: String): String = cryptographer.encryptString(token, secretKey)

    override fun decryptToken(cipherText: String, secretKey: String): String =
        cryptographer.decryptString(cipherText, secretKey)
}