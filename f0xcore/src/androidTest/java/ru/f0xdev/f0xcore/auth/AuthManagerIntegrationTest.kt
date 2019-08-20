package ru.f0xdev.f0xcore.auth

import android.accounts.AccountManager
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.auth.models.AccessToken
import ru.f0xdev.f0xcore.auth.models.AuthException
import ru.f0xdev.f0xcore.auth.net.IAuthRemoteDataSource
import ru.f0xdev.f0xcore.util.Cryptographer

@RunWith(AndroidJUnit4::class)
class AuthManagerIntegrationTest {
    private val context = InstrumentationRegistry.getTargetContext()
    private val conf = AuthConfig(
        context.getString(R.string.account_type),
        "acc_name",
        "api_key",
        "1"
    )
    private val am = AuthManager(
        AccountManager.get(context),
        conf,
        TokenCryptographer(Cryptographer())
    )
    private val testToken =
        AccessToken("acceesss", "bearer", System.currentTimeMillis(), "refresh", System.currentTimeMillis())


    @Test
    fun test_login_success() {
        val remoteDataSource = object : IAuthRemoteDataSource {
            override fun auth(login: String, password: String): AccessToken = testToken
            override fun refresh(refreshToken: String): AccessToken = testToken
        }

        initService(remoteDataSource)
        am.logout()
        val listenerStub = AuthEventListenerStub()
        am.addListener(listenerStub)
        am.login("1", "2")

        assertFalse(listenerStub.onLogoutCalled)
        assertTrue(listenerStub.onAccessUpdateCalled)
        assertTrue(listenerStub.onLoginCalled)
        am.removeListener(listenerStub)

    }

    @Test
    fun test_login_fail_network_error() {
        val remoteDataSource = object : IAuthRemoteDataSource {

            override fun auth(login: String, password: String): AccessToken {
                throw AuthException(AuthException.Type.NETWORK, "Network error")
            }

            override fun refresh(refreshToken: String): AccessToken = testToken
        }

        initService(remoteDataSource)
        am.logout()
        val listenerStub = AuthEventListenerStub()
        am.addListener(listenerStub)
        val ex: AuthException? = try {
            am.login("1", "2")
            null
        } catch (aue: AuthException) {
            aue
        }
        assertEquals(AuthException.Type.NETWORK, ex?.type)

        assertFalse(listenerStub.onLogoutCalled)
        assertFalse(listenerStub.onAccessUpdateCalled)
        assertFalse(listenerStub.onLoginCalled)
        am.removeListener(listenerStub)


    }

    @Test
    fun test_login_fail_wrong_credentials() {
        val remoteDataSource = object : IAuthRemoteDataSource {

            override fun auth(login: String, password: String): AccessToken {
                throw AuthException(AuthException.Type.WRONG_CREDENTIALS, "wrong creds")
            }

            override fun refresh(refreshToken: String): AccessToken = testToken
        }

        initService(remoteDataSource)
        am.logout()
        val listenerStub = AuthEventListenerStub()
        am.addListener(listenerStub)

        val ex: AuthException? = try {
            am.login("1", "2")
            null
        } catch (aue: AuthException) {
            aue
        }
        assertEquals(AuthException.Type.WRONG_CREDENTIALS, ex?.type)

        assertFalse(listenerStub.onLogoutCalled)
        assertFalse(listenerStub.onAccessUpdateCalled)
        assertFalse(listenerStub.onLoginCalled)
        am.removeListener(listenerStub)
    }

    @Test
    fun get_token_success() {
        val remoteDataSource = object : IAuthRemoteDataSource {

            override fun auth(login: String, password: String): AccessToken {
                return testToken
            }

            override fun refresh(refreshToken: String): AccessToken = testToken
        }

        initService(remoteDataSource)
        am.logout()
        val listenerStub = AuthEventListenerStub()
        am.addListener(listenerStub)
        am.login("1", "2")
        val token1 = am.getToken()
        assertEquals(testToken.accessToken, token1)
        val token2 = am.getToken()
        assertEquals(testToken.accessToken, token2)

        assertFalse(listenerStub.onLogoutCalled)
        assertTrue(listenerStub.onAccessUpdateCalled)
        assertTrue(listenerStub.onLoginCalled)
        am.removeListener(listenerStub)


    }

    @Test
    fun test_login_refresh_token_success() {
        val refreshedToken = AccessToken(
            "new_access_token",
            "Bearer ",
            System.currentTimeMillis(),
            "new_refresh_token",
            System.currentTimeMillis()
        )
        val remoteDataSource = object : IAuthRemoteDataSource {
            override fun auth(login: String, password: String): AccessToken {
                return testToken
            }

            override fun refresh(refreshToken: String): AccessToken = refreshedToken
        }
        initService(remoteDataSource)
        am.logout()
        am.login("1", "2")
        val listenerStub = AuthEventListenerStub()
        am.addListener(listenerStub)

        val token = am.getToken()
        assertEquals(testToken.accessToken, token)
        am.invalidateToken()
        val newToken = am.getToken()
        assertEquals(refreshedToken.accessToken, newToken)

        assertFalse(listenerStub.onLogoutCalled)
        assertTrue(listenerStub.onAccessUpdateCalled)
        assertFalse(listenerStub.onLoginCalled)
        am.removeListener(listenerStub)


    }

    @Test
    fun test_login_refresh_token_fail_network_errror() {
        val remoteDataSource = object : IAuthRemoteDataSource {
            override fun auth(login: String, password: String): AccessToken {
                return testToken
            }

            override fun refresh(refreshToken: String): AccessToken {
                throw AuthException(AuthException.Type.NETWORK, "network error")
            }
        }
        initService(remoteDataSource)
        am.logout()
        am.login("1", "2")
        val listenerStub = AuthEventListenerStub()
        am.addListener(listenerStub)

        val token = am.getToken()
        assertEquals(testToken.accessToken, token)
        val ex: AuthException? = try {
            am.invalidateToken()
            am.getToken()
            null
        } catch (aue: AuthException) {
            aue
        }

        assertFalse(listenerStub.onLogoutCalled)
        assertFalse(listenerStub.onAccessUpdateCalled)
        assertFalse(listenerStub.onLoginCalled)
        am.removeListener(listenerStub)
        assertEquals(AuthException.Type.NETWORK, ex?.type)

    }

    @Test
    fun test_login_refresh_token_fail_refresh_token_expired() {
        val remoteDataSource = object : IAuthRemoteDataSource {
            override fun auth(login: String, password: String): AccessToken {
                return testToken
            }

            override fun refresh(refreshToken: String): AccessToken {
                throw AuthException(AuthException.Type.REFRESH_TOKEN_EXPIRED, "refresh error ")
            }
        }
        initService(remoteDataSource)
        am.logout()
        am.login("1", "2")
        val listener = AuthEventListenerStub()
        am.addListener(listener)
        val token = am.getToken()
        assertEquals(testToken.accessToken, token)
        val ex: AuthException? =
            try {
                am.invalidateToken()
                am.getToken()
                null
            } catch (aue: AuthException) {
                aue
            }
        assertEquals(AuthException.Type.REFRESH_TOKEN_EXPIRED, ex?.type)
        assertTrue(listener.onLogoutCalled)
        am.removeListener(listener)
    }

    private fun initService(
        remoteDataSource: IAuthRemoteDataSource,
        cryptographer: ITokenCryptographer = TokenCryptographer(Cryptographer())
    ) {

        AuthenticatorServiceTestStub.remoteDataSource = remoteDataSource
        AuthenticatorServiceTestStub.cryptographer = cryptographer
    }
}