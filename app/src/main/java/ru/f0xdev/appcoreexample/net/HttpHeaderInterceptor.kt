package ru.f0xdev.appcoreexample.net

import okhttp3.Request
import ru.f0xdev.f0xcore.auth.IAuthManager
import ru.f0xdev.f0xcore.net.ABaseHeaderInterceptor
import ru.f0xdev.f0xcore.net.IResponseErrorProcessor
import ru.f0xdev.f0xcore.net.IUserAgentProvider

class HttpHeaderInterceptor(
    private val userAgentProvider: IUserAgentProvider,
    private val authManager: IAuthManager,
    errorProcessors: Map<Int, IResponseErrorProcessor>
) : ABaseHeaderInterceptor(errorProcessors) {
    companion object {
        internal const val HEADER_AUTH_KEY = "Authorization"
    }

    override fun addAdditionalHeaders(builder: Request.Builder): Request.Builder {
        val ua = userAgentProvider.provideUserAgent()
        builder.addHeader(ua.headerKey, ua.userAgentValue)
        if (authManager.isAuthenticated()) {
            val authType = authManager.getAuthType()
            if (authType != null)
                builder.addHeader(HEADER_AUTH_KEY, "$authType ${authManager.getToken()}")
            else
                builder.addHeader(HEADER_AUTH_KEY, authManager.getToken())
        }
        return builder
    }
}