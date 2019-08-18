package ru.f0xdev.f0xcore.auth

class TokenCryptographerTestStub : ITokenCryptographer {
    override fun encryptToken(token: String, secretKey: String): String = token

    override fun decryptToken(cipherText: String, secretKey: String): String = cipherText
}