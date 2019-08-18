package ru.f0xdev.f0xcore.auth.net

import finance.robo.android.accountservice.models.AuthException
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

class ABaseHeaderInterceptorAndUnauthorizedErrorProcessorTest {
    private val authType = "Bearer"

    @Test
    fun `test process request success add api key in header`() {
        var logoutCalled = false
        var isAuthCalled = false
        var inValidateCalled = false
        val am = object : AuthManagerTestStub() {
            override fun logout() {
                logoutCalled = true
            }

            override fun isAuthenticated(): Boolean {
                isAuthCalled = true
                return false
            }

            override fun invalidateToken() {
                inValidateCalled = true
            }
        }
        val interceptor = HeaderInterceptorTest(am, mapOf(), am.getApiKey())
        val chain = object : BaseInterceptorChain() {
            override fun proceed(request: Request): Response {
                assertEquals(
                    request.header(HeaderInterceptorTest.TEST_HEADER_KEY),
                    HeaderInterceptorTest.TEST_HEADER_VALUE
                )
                assertEquals(request.header(ABaseHeaderInterceptor.HEADER_AUTH_KEY), am.getApiKey())
                return Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("success")
                    .build()
            }

            override fun request(): Request {
                return Request.Builder()
                    .url("http://localhost")
                    .get()
                    .build()
            }
        }
        interceptor.intercept(chain)
        assertTrue(isAuthCalled)
        assertFalse(inValidateCalled)
        assertFalse(logoutCalled)
    }

    @Test
    fun `test process request success add token in header`() {
        val am = object : AuthManagerTestStub() {
            var logoutCalled = false
            var isAuthCalled = false
            var inValidateCalled = false
            override fun logout() {
                logoutCalled = true
            }

            override fun isAuthenticated(): Boolean {
                isAuthCalled = true
                return true
            }

            override fun invalidateToken() {
                inValidateCalled = true
            }
        }
        val interceptor = HeaderInterceptorTest(am, mapOf(), am.getApiKey())
        val chain = object : BaseInterceptorChain() {
            override fun proceed(request: Request): Response {
                assertEquals(
                    request.header(HeaderInterceptorTest.TEST_HEADER_KEY),
                    HeaderInterceptorTest.TEST_HEADER_VALUE
                )
                assertEquals(request.header(ABaseHeaderInterceptor.HEADER_AUTH_KEY), authType + " " + am.getToken())
                return Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("success")
                    .build()
            }

            override fun request(): Request {
                return Request.Builder()
                    .url("http://localhost")
                    .get()
                    .build()
            }
        }
        interceptor.intercept(chain)
        assertTrue(am.isAuthCalled)
        assertFalse(am.inValidateCalled)
        assertFalse(am.logoutCalled)
    }


    @Test
    fun `test process request and refresh token success`() {

        val am = object : AuthManagerTestStub() {

            var logoutCalled = false
            var isAuthCalled = false
            var inValidateCalled = false
            override fun logout() {
                logoutCalled = true
            }

            override fun isAuthenticated(): Boolean {
                isAuthCalled = true
                return true
            }

            override fun invalidateToken() {
                inValidateCalled = true
            }
        }
        var proceedCount = 0
        val interceptor =
            HeaderInterceptorTest(
                am,
                mapOf(401 to UnauthorizedErrorProcessor(am)),
                am.getApiKey()
            )
        val chain = object : BaseInterceptorChain() {
            override fun proceed(request: Request): Response {
                proceedCount++
                assertEquals(
                    request.header(HeaderInterceptorTest.TEST_HEADER_KEY),
                    HeaderInterceptorTest.TEST_HEADER_VALUE
                )
                assertEquals(request.header(ABaseHeaderInterceptor.HEADER_AUTH_KEY), authType + " " + am.getToken())

                return if (proceedCount == 1)
                    Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(401)
                        .message("unauthorized")
                        .build()
                else
                    Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("success")
                        .build()
            }

            override fun request(): Request {
                return Request.Builder()
                    .url("http://localhost")
                    .get()
                    .build()
            }
        }
        val response = interceptor.intercept(chain)
        assertTrue(am.isAuthCalled)
        assertTrue(am.inValidateCalled)
        assertFalse(am.logoutCalled)
        assertEquals(2, proceedCount)
        assertEquals(200, response.code())
    }


    @Test
    fun `test process request and refresh token expired`() {
        val am = object : AuthManagerTestStub() {
            var isAuthCalled = false
            var inValidateCalled = false


            override fun isAuthenticated(): Boolean {
                isAuthCalled = true
                return true
            }

            override fun invalidateToken() {
                inValidateCalled = true
            }

            override fun getToken(): String {
                if (inValidateCalled) {
                    throw AuthException(AuthException.Type.REFRESH_TOKEN_EXPIRED, "token expired")
                }
                return super.getToken()
            }

        }

        var proceedCount = 0
        val interceptor =
            HeaderInterceptorTest(
                am,
                mapOf(401 to UnauthorizedErrorProcessor(am)),
                am.getApiKey()
            )
        val chain = object : BaseInterceptorChain() {
            override fun proceed(request: Request): Response {
                proceedCount++
                assertEquals(
                    request.header(HeaderInterceptorTest.TEST_HEADER_KEY),
                    HeaderInterceptorTest.TEST_HEADER_VALUE
                )
                assertEquals(request.header(ABaseHeaderInterceptor.HEADER_AUTH_KEY), authType + " " + am.getToken())
                return Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(401)
                    .message("unauthorized")
                    .build()

            }

            override fun request(): Request {
                return Request.Builder()
                    .url("http://localhost")
                    .get()
                    .build()
            }
        }
        val response = interceptor.intercept(chain)
        assertTrue(am.isAuthCalled)
        assertTrue(am.inValidateCalled)
        assertEquals(1, proceedCount)
        assertEquals(401, response.code())
    }

    @Test(expected = IOException::class)
    fun `test process request and refresh token network error`() {

        val am = object : AuthManagerTestStub() {
            var logoutCalled = false
            var isAuthCalled = false
            var inValidateCalled = false

            override fun logout() {
                logoutCalled = true
            }

            override fun isAuthenticated(): Boolean {
                isAuthCalled = true
                return true
            }

            override fun invalidateToken() {
                inValidateCalled = true
            }

            override fun getToken(): String {
                if (inValidateCalled) {
                    throw AuthException(AuthException.Type.NETWORK, "Network test error")
                }
                return super.getToken()
            }

        }
        var proceedCount = 0
        val interceptor =
            HeaderInterceptorTest(
                am,
                mapOf(401 to UnauthorizedErrorProcessor(am)),
                am.getApiKey()
            )
        val chain = object : BaseInterceptorChain() {
            override fun proceed(request: Request): Response {
                proceedCount++
                assertEquals(
                    request.header(HeaderInterceptorTest.TEST_HEADER_KEY),
                    HeaderInterceptorTest.TEST_HEADER_VALUE
                )
                assertEquals(request.header(ABaseHeaderInterceptor.HEADER_AUTH_KEY), authType + " " + am.getToken())
                return Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(401)
                    .message("unauthorized")
                    .build()

            }

            override fun request(): Request {
                return Request.Builder()
                    .url("http://localhost")
                    .get()
                    .build()
            }
        }

        interceptor.intercept(chain)

    }

}





