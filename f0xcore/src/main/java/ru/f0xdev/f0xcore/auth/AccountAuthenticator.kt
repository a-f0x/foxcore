package ru.f0xdev.f0xcore.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.google.gson.Gson
import finance.robo.android.accountservice.models.AuthException
import ru.f0xdev.f0xcore.auth.net.IAuthRemoteDataSource
import java.io.IOException


class AccountAuthenticator(
    context: Context,
    private val accountManager: AccountManager,
    private val authRemoteDataSource: IAuthRemoteDataSource,
    private val cryptographer: ITokenCryptographer
) : AbstractAccountAuthenticator(context) {
    companion object {

        internal const val LOGIN_KEY = "login_key"
        internal const val PASSWORD_KEY = "password_key"
        internal const val ACCOUNT_CREATE_TIME_KEY = "account_create_time_key"
        internal const val AUTH_TOKEN_TYPE_KEY = "auth_token_type_key"
        private const val DEFAULT_CRYPT = "default_secret_k" // надо  что бы длина была именно 16
        const val AUTH_EXCEPTION_KEY = "auth_exception_key"

        @SuppressLint("MissingPermission")
        internal fun getCryptKey(accountManager: AccountManager, account: Account): String {
            return accountManager.getUserData(account, ACCOUNT_CREATE_TIME_KEY) ?: DEFAULT_CRYPT
        }
    }


    private val gson: Gson = Gson()

    @SuppressLint("MissingPermission")
    override fun getAuthToken(
        response: AccountAuthenticatorResponse?,
        account: Account,
        authTokenType: String?,
        options: Bundle
    ): Bundle {
        val result = Bundle()

        val cryptKey = getCryptKey(accountManager, account)
        val password = accountManager.getPassword(account)
        val token = try {
            if (password == null) {
                val login = options.getString(LOGIN_KEY)
                if (login.isNullOrEmpty())
                    throw AuthException(AuthException.Type.WRONG_CREDENTIALS, "Login is null")
                val p = options.getString(PASSWORD_KEY)
                if (p.isNullOrEmpty())
                    throw AuthException(AuthException.Type.WRONG_CREDENTIALS, "Password is null")
                authRemoteDataSource.auth(login, p)
            } else {
                val refreshToken =
                    cryptographer.decryptToken(password, cryptKey)
                authRemoteDataSource.refresh(refreshToken)
            }
        } catch (aue: AuthException) {
            result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false)
            result.putString(AUTH_EXCEPTION_KEY, gson.toJson(aue))
//            if (aue.type == AuthException.Type.REFRESH_TOKEN_EXPIRED) {
//                accountManager.removeAccount(account, null, null).result
//            }
            return result
        } catch (ioe: IOException) {
            result.apply {
                putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false)
                putString(
                    AUTH_EXCEPTION_KEY,
                    gson.toJson(AuthException(AuthException.Type.NETWORK, ioe.message ?: "Network error"))
                )
            }
            return result
        }
        val encryptedToken = cryptographer.encryptToken(token.accessToken, cryptKey)
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true)
        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
        result.putString(AccountManager.KEY_AUTHTOKEN, token.accessToken)

        token.refreshToken?.let {
            accountManager.setPassword(account, cryptographer.encryptToken(it, cryptKey))
        }

        accountManager.setAuthToken(account, authTokenType, encryptedToken)
        val tokenType = token.type
        if (tokenType != null)
            accountManager.setUserData(account, AUTH_TOKEN_TYPE_KEY, tokenType)
        return result
    }


    override fun getAuthTokenLabel(authTokenType: String?): String? = null

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        options: Bundle?
    ): Bundle = Bundle.EMPTY

    override fun updateCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ) = Bundle.EMPTY


    override fun hasFeatures(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        features: Array<out String>?
    ) = Bundle.EMPTY

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?) = Bundle.EMPTY

    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ) = Bundle.EMPTY


}