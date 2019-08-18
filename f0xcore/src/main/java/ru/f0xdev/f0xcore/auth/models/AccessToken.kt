package ru.f0xdev.f0xcore.auth.models

import com.google.gson.annotations.SerializedName

data class AccessToken(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val type: String?,
    @SerializedName("expires_in")
    val expiresIn: Long = 0,
    @SerializedName("refresh_token")
    val refreshToken: String?,
    @SerializedName("create_date")
    val createDate: Long
)
