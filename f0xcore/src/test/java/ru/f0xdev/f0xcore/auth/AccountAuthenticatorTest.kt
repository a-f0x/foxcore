package ru.f0xdev.f0xcore.auth

import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.os.Bundle
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import ru.f0xdev.f0xcore.auth.models.AccessToken
import ru.f0xdev.f0xcore.auth.models.AuthException
import ru.f0xdev.f0xcore.auth.net.IAuthRemoteDataSource
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class AccountAuthenticatorTest {
    private val context = RuntimeEnvironment.application.applicationContext
    private val am = AccountManager.get(context)
    private val cryptographer = TokenCryptographerTestStub()
    private val gson = Gson()
    private val conf = AuthConfig("acc_type", "acc_name", "api_key", "1")

    private val testToken =
        AccessToken("acceesss", "bearer", System.currentTimeMillis(), "refresh", System.currentTimeMillis())
    private val account = Account(conf.accountName, conf.accountType)


    @Test
    fun `get auth token with login password`() {
        val logintest = "login"
        val passtest = "pass"

        val remote = object : IAuthRemoteDataSource {
            override fun auth(login: String, password: String): AccessToken {
                assertEquals(logintest, login)
                assertEquals(passtest, password)
                return testToken
            }

            override fun refresh(refreshToken: String): AccessToken = testToken
        }

        am.addAccountExplicitly(account, null, Bundle.EMPTY)
        val aa = AccountAuthenticator(context, am, remote, cryptographer)
        val mockBundle = mock(Bundle::class.java)
        whenever(mockBundle.getString(AccountAuthenticator.PASSWORD_KEY)).thenReturn(passtest)
        whenever(mockBundle.getString(AccountAuthenticator.LOGIN_KEY)).thenReturn(logintest)
        val response = mock(AccountAuthenticatorResponse::class.java)

        val result = aa.getAuthToken(response, account, "tokenType", mockBundle)
        assertNotNull(result)
        assertTrue(result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT))
        assertEquals(testToken.accessToken, result.getString(AccountManager.KEY_AUTHTOKEN))

    }

    @Test
    fun `get auth token with  password (refresh)`() {

        val remote = object : IAuthRemoteDataSource {
            override fun auth(login: String, password: String): AccessToken = testToken
            override fun refresh(refreshToken: String): AccessToken {
                assertEquals(testToken.refreshToken, refreshToken)
                return testToken
            }
        }

        val aa = AccountAuthenticator(context, am, remote, cryptographer)
        am.addAccountExplicitly(account, testToken.refreshToken, Bundle.EMPTY)
        val response = mock(AccountAuthenticatorResponse::class.java)

        val result = aa.getAuthToken(response, account, "tokenType", Bundle.EMPTY)
        assertNotNull(result)
        assertTrue(result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT))
        assertEquals(testToken.accessToken, result.getString(AccountManager.KEY_AUTHTOKEN))
    }

    @Test
    fun `test auth with login password failed (wrong credentials)`() {
        val logintest = "login"
        val passtest = "pass"

        val remote = object : IAuthRemoteDataSource {
            override fun auth(login: String, password: String): AccessToken {
                assertEquals(logintest, login)
                assertEquals(passtest, password)
                throw AuthException(AuthException.Type.WRONG_CREDENTIALS, "wrong creds")
            }

            override fun refresh(refreshToken: String): AccessToken = testToken
        }

        val aa = AccountAuthenticator(context, am, remote, cryptographer)

        am.addAccountExplicitly(account, null, Bundle.EMPTY)
        val mockBundle = Bundle().apply {
            putString(AccountAuthenticator.PASSWORD_KEY, passtest)
            putString(AccountAuthenticator.LOGIN_KEY, logintest)
        }
        val response = mock(AccountAuthenticatorResponse::class.java)

        val result = aa.getAuthToken(response, account, "tokenType", mockBundle)
        assertNotNull(result)
        assertFalse(result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT))
        val aue = gson.fromJson<AuthException>(
            result.getString(AccountAuthenticator.AUTH_EXCEPTION_KEY),
            AuthException::class.java
        )
        assertEquals(AuthException.Type.WRONG_CREDENTIALS, aue.type)
    }

    @Test
    fun `test auth with login password failed (network error)`() {
        val logintest = "login"
        val passtest = "pass"

        val remote = object : IAuthRemoteDataSource {
            override fun auth(login: String, password: String): AccessToken {
                assertEquals(logintest, login)
                assertEquals(passtest, password)
                throw IOException("network error")
            }

            override fun refresh(refreshToken: String): AccessToken = testToken
        }

        val aa = AccountAuthenticator(context, am, remote, cryptographer)

        am.addAccountExplicitly(account, null, Bundle.EMPTY)
        val mockBundle = Bundle().apply {
            putString(AccountAuthenticator.PASSWORD_KEY, passtest)
            putString(AccountAuthenticator.LOGIN_KEY, logintest)
        }

        val response = mock(AccountAuthenticatorResponse::class.java)
        val result = aa.getAuthToken(response, account, "tokenType", mockBundle)
        assertNotNull(result)
        assertFalse(result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT))
        val aue = gson.fromJson<AuthException>(
            result.getString(AccountAuthenticator.AUTH_EXCEPTION_KEY),
            AuthException::class.java
        )
        assertEquals(AuthException.Type.NETWORK, aue.type)
    }

    @Test
    fun `get auth token with  password (refresh) fail refresh token expired`() {

        val remote = object : IAuthRemoteDataSource {
            override fun auth(login: String, password: String): AccessToken {
                return testToken
            }

            override fun refresh(refreshToken: String): AccessToken {
                throw AuthException(AuthException.Type.REFRESH_TOKEN_EXPIRED, "token expired")
            }
        }

        am.addAccountExplicitly(account, testToken.refreshToken, Bundle.EMPTY)
        val aa = AccountAuthenticator(context, am, remote, cryptographer)
        val mockBundle = Bundle()
        val response = mock(AccountAuthenticatorResponse::class.java)

        val result = aa.getAuthToken(response, account, "tokenType", mockBundle)
        assertNotNull(result)
        assertFalse(result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT))
        val aue = gson.fromJson<AuthException>(
            result.getString(AccountAuthenticator.AUTH_EXCEPTION_KEY),
            AuthException::class.java
        )
        assertEquals(AuthException.Type.REFRESH_TOKEN_EXPIRED, aue.type)
    }
}