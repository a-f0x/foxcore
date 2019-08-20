package ru.f0xdev.f0xcore.auth.net

import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Test

class ABaseHeaderInterceptorTest {

    @Test
    fun `test process request success add custom header`() {
        val interceptor = HeaderInterceptorTest()
        val chain = object : BaseInterceptorChain() {
            override fun proceed(request: Request): Response {
                assertEquals(
                    request.header(HeaderInterceptorTest.TEST_HEADER_KEY),
                    HeaderInterceptorTest.TEST_HEADER_VALUE
                )
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
    }
}





