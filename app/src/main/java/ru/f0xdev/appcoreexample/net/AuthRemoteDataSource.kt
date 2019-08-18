package ru.f0xdev.appcoreexample.net

import com.google.gson.annotations.SerializedName
import finance.robo.android.accountservice.models.AuthException
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.f0xdev.f0xcore.auth.models.AccessToken
import ru.f0xdev.f0xcore.auth.net.IAuthRemoteDataSource
import ru.f0xdev.f0xcore.util.logError
import java.io.IOException

class AuthRemoteDataSource(
    private val remote: AuthRemoteDataSourceRetrofit
) : IAuthRemoteDataSource {
    override fun auth(login: String, password: String): AccessToken {
        val response = try {
            remote.auth(AuthRemoteDataSourceRetrofit.AuthByPasswordRequest(login, password))
                .execute()

        } catch (ioe: IOException) {
            this.logError("Error auth ${ioe.message}", ioe)
            throw AuthException(AuthException.Type.NETWORK, ioe.message ?: "Network error")
        }
        if (response.isSuccessful) {
            val token = response.body() ?: throw AuthException(
                AuthException.Type.UNKNOWN_ERROR,
                "Error get token from response"
            )
            return token.mapToAccessToken()
        } else {
            when (response.code()) {
                401 -> {
                    throw AuthException(AuthException.Type.WRONG_CREDENTIALS, "Wrong credentials")
                }
                else -> {
                    throw AuthException(AuthException.Type.UNKNOWN_ERROR, "Unknown error")
                }
            }
        }
    }

    override fun refresh(refreshToken: String): AccessToken {
        throw AuthException(AuthException.Type.REFRESH_TOKEN_EXPIRED, "Refresh token expired")
    }

}

data class ReqresInAccessTokenResponse(
    @SerializedName("token")
    val token: String
) {
    fun mapToAccessToken(): AccessToken {
        return AccessToken(
            accessToken = token,
            type = null,
            refreshToken = null,
            expiresIn = 86400,
            createDate = System.currentTimeMillis()
        )
    }
}


interface AuthRemoteDataSourceRetrofit {

    @POST("/api/login")
    fun auth(@Body authByPasswordRequest: AuthByPasswordRequest): Call<ReqresInAccessTokenResponse>

    data class AuthByPasswordRequest(
        @SerializedName("email")
        val username: String,
        @SerializedName("password")
        val password: String
    )
}
