package ru.f0xdev.f0xcore.auth.models

data class AccessToken(
    val accessToken: String,
    val type: String?,
    val expiresIn: Long = 0,
    val refreshToken: String?,
    val createDate: Long = 0
)
