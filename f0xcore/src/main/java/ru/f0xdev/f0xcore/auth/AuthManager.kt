package ru.f0xdev.f0xcore.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.os.Bundle
import com.google.gson.Gson
import finance.robo.android.accountservice.models.AuthException
import ru.f0xdev.f0xcore.auth.models.AccessToken
import java.util.*
import kotlin.collections.ArrayList


class AuthManager(
    private val accountManager: AccountManager,
    private val config: AuthConfig,
    private val cryptographer: ITokenCryptographer
) : IAuthManager {

    @SuppressLint("MissingPermission")
    override fun getAuthType(): String? {
        getAccount()?.let { acc ->
            return accountManager.getUserData(acc, AccountAuthenticator.AUTH_TOKEN_TYPE_KEY)
        }
        return null
    }

    private val listeners = Collections.synchronizedCollection(ArrayList<IAuthEventListener>())

    override fun addListener(listener: IAuthEventListener) {
        if (!listeners.contains(listener))
            listeners.add(listener)
    }

    override fun removeListener(listener: IAuthEventListener) {
        listeners.remove(listener)
    }

    override fun getApiKey(): String {
        return config.apiKey
    }

    private val gson = Gson()

    companion object {
        internal const val TOKEN_TYPE = "all"
    }

    @Throws(AuthException::class)
    override fun login(username: String, password: String) {
        val options = loginOptions(username, password)
        val acc = getAccountOrCreate()
        getAuthToken(acc, options)
        listeners.forEach {
            it.onLogin()
        }
    }

    @SuppressLint("MissingPermission")
    override fun logout() {
        val acc = getAccount() ?: return
        accountManager.removeAccount(acc, null, null).result
        listeners.forEach {
            it.onLogout()
        }
    }

    @SuppressLint("MissingPermission")
    @Synchronized
    override fun invalidateToken() {
        getCachedToken()?.let {
            accountManager.invalidateAuthToken(config.accountType, it)
        }
    }

    @Synchronized
    override fun getToken(): String {
        try {
            return getCachedToken()!!
        } catch (aue: AuthException) {
            if (aue.type == AuthException.Type.REFRESH_TOKEN_EXPIRED)
                logout()
            throw aue
        }
    }


    @SuppressLint("MissingPermission")
    override fun isAuthenticated(): Boolean {
        val acc = getAccount() ?: return false
        val token = accountManager.peekAuthToken(acc, TOKEN_TYPE)
        return token != null
    }

    @SuppressLint("MissingPermission")
    @Synchronized
    override fun setAccessToken(accessToken: AccessToken) {
        val account = getAccountOrCreate()
        val key = AccountAuthenticator.getCryptKey(accountManager, account)
        val access = cryptographer.encryptToken(accessToken.accessToken, key)
        accountManager.setAuthToken(account, TOKEN_TYPE, access)
        accessToken.refreshToken?.let {
            accountManager.setPassword(account, cryptographer.encryptToken(it, key))
        }
    }

    @SuppressLint("MissingPermission")
    @Throws(AuthException::class)
    @Synchronized
    private fun getAuthToken(acc: Account, options: Bundle): String {
        val result = accountManager.getAuthToken(
            acc,
            TOKEN_TYPE,
            options,
            false,
            null,
            null
        ).result
        val resultOk = result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)
        if (resultOk) {
            listeners.forEach {
                it.onAccessTokenUpdated()
            }
            return result.getString(AccountManager.KEY_AUTHTOKEN)!!
        } else {
            val aue = gson.fromJson<AuthException>(
                result.getString(AccountAuthenticator.AUTH_EXCEPTION_KEY),
                AuthException::class.java
            )
            throw aue
        }
    }

    private fun getAccountOrCreate(): Account {
        return getAccount() ?: addAccount(config.accountName, config.accountType)
    }

    @SuppressLint("MissingPermission")
    private fun addAccount(accountName: String, accountType: String): Account {
        val createAccountTime = "key${Date().time}"
        val account = Account(accountName, accountType)
        accountManager.addAccountExplicitly(account, null, Bundle.EMPTY)
        accountManager.setUserData(account, AccountAuthenticator.ACCOUNT_CREATE_TIME_KEY, createAccountTime)
        return account
    }

    private fun loginOptions(login: String, password: String): Bundle {
        val options = Bundle()
        options.putString(AccountAuthenticator.LOGIN_KEY, login)
        options.putString(AccountAuthenticator.PASSWORD_KEY, password)
        return options
    }

    private fun refreshOptions(): Bundle {
        return Bundle()
    }


    @SuppressLint("MissingPermission")
    private fun getAccount(): Account? {
        val accounts = accountManager.getAccountsByType(config.accountType)
        return if (accounts.isEmpty())
            null
        else {
            accounts[0]
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCachedToken(): String? {
        val acc = getAccount()
        if (acc != null) {
            val token = accountManager.peekAuthToken(acc, TOKEN_TYPE)
            if (token != null)
                return token
            return getAuthToken(acc, refreshOptions())
        }

        return null
    }
}