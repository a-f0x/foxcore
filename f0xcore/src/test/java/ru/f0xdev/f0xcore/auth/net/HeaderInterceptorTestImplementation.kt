package ru.f0xdev.f0xcore.auth.net

import okhttp3.Request
import ru.f0xdev.f0xcore.net.ABaseHeaderInterceptor

class HeaderInterceptorTest : ABaseHeaderInterceptor(emptyMap()) {
    companion object {
        const val TEST_HEADER_KEY = "test_header_key"
        const val TEST_HEADER_VALUE = "test header value"
    }

    override fun addAdditionalHeaders(builder: Request.Builder): Request.Builder {
        builder.addHeader(
            TEST_HEADER_KEY,
            TEST_HEADER_VALUE
        )
        return builder
    }
}
