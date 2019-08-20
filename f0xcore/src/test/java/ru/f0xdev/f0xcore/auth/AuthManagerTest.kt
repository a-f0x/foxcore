package ru.f0xdev.f0xcore.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.os.Bundle
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import ru.f0xdev.f0xcore.auth.models.AccessToken
import ru.f0xdev.f0xcore.auth.models.AuthException
import ru.f0xdev.f0xcore.setFieldValue
import java.util.*
import java.util.concurrent.TimeUnit

class AuthManagerTest {
    private val am: AccountManager = mock<AccountManager>(AccountManager::class.java)
    private val cryptographer = TokenCryptographerTestStub()

    private val conf = AuthConfig("acc_type", "acc_name", "api_key", "1")
    private val testAccessToken =
        AccessToken("access_token", "Bearer", System.currentTimeMillis(), "refresh_token", Date().time)

    private val gson = Gson()

    private val account = Account(conf.accountName, conf.accountType).apply {
        setFieldValue(Account::class.java.getDeclaredField("name"), conf.accountName)
        setFieldValue(Account::class.java.getDeclaredField("type"), conf.accountType)

    }

    init {
        whenever(
            am.addAccount(
                eq(conf.accountType),
                eq(AuthManager.TOKEN_TYPE),
                eq(null),
                any(),
                eq(null),
                any(),
                eq(null)
            )
        )
            .thenReturn(object : AccountManagerFuture<Bundle> {
                override fun getResult(timeout: Long, unit: TimeUnit?): Bundle {
                    return Bundle.EMPTY
                }

                override fun isDone(): Boolean = true
                override fun cancel(mayInterruptIfRunning: Boolean): Boolean = true
                override fun isCancelled(): Boolean = false
                override fun getResult(): Bundle = Bundle()
            })

        whenever(am.getAccountsByType(eq(conf.accountType))).thenReturn(arrayOf(account))
    }


    @Test
    fun getApiKey() {
        val authManager = AuthManager(am, conf, cryptographer)
        assertEquals(conf.apiKey, authManager.getApiKey())
    }

    @Test
    fun `login success`() {
        val listener = mock(IAuthEventListener::class.java)


        val bundle = mock(Bundle::class.java)
        whenever(bundle.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)).thenReturn(true)
        whenever(bundle.getString(AccountManager.KEY_AUTHTOKEN)).thenReturn(testAccessToken.accessToken)
        whenever(
            am.getAuthToken(
                Mockito.any(Account::class.java),
                eq(AuthManager.TOKEN_TYPE),
                Mockito.any(Bundle::class.java),
                eq(false),
                eq(null),
                eq(null)
            )
        ).thenReturn(object : AccountManagerFuture<Bundle> {
            override fun getResult(timeout: Long, unit: TimeUnit?): Bundle {
                return Bundle.EMPTY
            }

            override fun isDone(): Boolean = true
            override fun cancel(mayInterruptIfRunning: Boolean): Boolean = true
            override fun isCancelled(): Boolean = false
            override fun getResult(): Bundle = bundle
        })
        whenever(
            am.removeAccount(
                Mockito.any(Account::class.java),
                eq(null),
                eq(null)
            )
        ).thenReturn(object : AccountManagerFuture<Boolean> {
            override fun getResult(): Boolean = true

            override fun getResult(timeout: Long, unit: TimeUnit?): Boolean = true

            override fun isDone(): Boolean = true

            override fun cancel(mayInterruptIfRunning: Boolean): Boolean = false

            override fun isCancelled(): Boolean = false
        })

        val authManager = AuthManager(am, conf, cryptographer).apply {
            addListener(listener)
        }
        authManager.login("login", "password")


        verify(listener, times(1)).onAccessTokenUpdated()
        verify(listener, times(1)).onLogin()
        verify(listener, times(0)).onLogout()
    }

    @Test(expected = AuthException::class)
    fun `login fail`() {
        val ae = AuthException(AuthException.Type.NETWORK, "network error")

        val listener = mock(IAuthEventListener::class.java)

        val bundle = mock(Bundle::class.java)
        whenever(bundle.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)).thenReturn(false)
        whenever(bundle.getString(AccountAuthenticator.AUTH_EXCEPTION_KEY)).thenReturn(
            gson.toJson(ae)
        )

        whenever(
            am.getAuthToken(
                Mockito.any(Account::class.java),
                eq(AuthManager.TOKEN_TYPE),
                Mockito.any(Bundle::class.java),
                eq(false),
                eq(null),
                eq(null)
            )
        ).thenReturn(object : AccountManagerFuture<Bundle> {
            override fun getResult(timeout: Long, unit: TimeUnit?): Bundle {
                return bundle
            }

            override fun isDone(): Boolean = true
            override fun cancel(mayInterruptIfRunning: Boolean): Boolean = true
            override fun isCancelled(): Boolean = false
            override fun getResult(): Bundle = bundle
        })
        whenever(
            am.removeAccount(
                Mockito.any(Account::class.java),
                eq(null),
                eq(null)
            )
        ).thenReturn(object : AccountManagerFuture<Boolean> {
            override fun getResult(): Boolean = true

            override fun getResult(timeout: Long, unit: TimeUnit?): Boolean = true

            override fun isDone(): Boolean = true

            override fun cancel(mayInterruptIfRunning: Boolean): Boolean = false

            override fun isCancelled(): Boolean = false
        })


        val authManager = AuthManager(am, conf, cryptographer).apply {
            addListener(listener)
        }
        try {
            authManager.login("login", "password")
        } catch (aue: AuthException) {
            verify(listener, times(0)).onAccessTokenUpdated()
            verify(listener, times(0)).onLogin()
            verify(listener, times(0)).onLogout()
            assertEquals(ae.type, aue.type)
            assertEquals(ae.message, aue.message)
            throw aue
        }

    }

    @Test
    fun logout() {
        val listener = mock(IAuthEventListener::class.java)

        whenever(
            am.removeAccount(
                Mockito.any(Account::class.java),
                eq(null),
                eq(null)
            )
        ).thenReturn(object : AccountManagerFuture<Boolean> {
            override fun getResult(): Boolean = true

            override fun getResult(timeout: Long, unit: TimeUnit?): Boolean = true

            override fun isDone(): Boolean = true

            override fun cancel(mayInterruptIfRunning: Boolean): Boolean = false

            override fun isCancelled(): Boolean = false
        })


        val authManager = AuthManager(am, conf, cryptographer).apply {
            addListener(listener)
        }
        authManager.logout()
        verify(listener, never()).onAccessTokenUpdated()
        verify(listener, never()).onLogin()
        verify(listener).onLogout()
    }

    @Test(expected = Test.None::class)
    fun invalidateToken() {
        val listener = mock(IAuthEventListener::class.java)

        val authManager = AuthManager(am, conf, cryptographer).apply {
            addListener(listener)
        }
        whenever(
            am.peekAuthToken(
                eq(account),
                eq(AuthManager.TOKEN_TYPE)
            )
        ).thenReturn(testAccessToken.accessToken)
        authManager.invalidateToken()
        verify(listener, times(0)).onAccessTokenUpdated()
        verify(listener, times(0)).onLogin()
        verify(listener, times(0)).onLogout()
        val captor = ArgumentCaptor.forClass(String::class.java)
        verify(am, times(1)).invalidateAuthToken(captor.capture(), captor.capture())
        val stringArgs = captor.allValues
        val tokenType = stringArgs[0]
        val accessToken = stringArgs[1]
        assertEquals(conf.accountType, tokenType)
        assertEquals(testAccessToken.accessToken, accessToken)

        verify(listener, times(0)).onAccessTokenUpdated()
        verify(listener, times(0)).onLogin()
        verify(listener, times(0)).onLogout()

    }

    @Test
    fun getToken() {
        whenever(
            am.peekAuthToken(
                eq(account),
                eq(AuthManager.TOKEN_TYPE)
            )
        ).thenReturn(testAccessToken.accessToken)

        val listener = mock(IAuthEventListener::class.java)

        val authManager = AuthManager(am, conf, cryptographer).apply {
            addListener(listener)
        }
        assertEquals(authManager.getToken(), testAccessToken.accessToken)

        verify(listener, times(0)).onAccessTokenUpdated()
        verify(listener, times(0)).onLogin()
        verify(listener, times(0)).onLogout()
    }

    @Test
    fun `is authenticated success`() {

        val authManager = AuthManager(am, conf, cryptographer)
        whenever(
            am.peekAuthToken(
                eq(account),
                eq(AuthManager.TOKEN_TYPE)
            )
        ).thenReturn(testAccessToken.accessToken)

        assertTrue(authManager.isAuthenticated())

    }

    @Test
    fun `is authenticated fail`() {
        val authManager = AuthManager(am, conf, cryptographer)
        whenever(am.getAccountsByType(eq(conf.accountType))).thenReturn(emptyArray())
        assertFalse(authManager.isAuthenticated())
    }

}