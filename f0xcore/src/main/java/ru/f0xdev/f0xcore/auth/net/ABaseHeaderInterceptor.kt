package ru.f0xdev.f0xcore.auth.net

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.f0xdev.f0xcore.auth.IAuthManager

abstract class ABaseHeaderInterceptor(
    protected val authManager: IAuthManager,
    var errorProcessors: Map<Int, IResponseErrorProcessor>,
    protected val apiKey: String?

) : Interceptor {

    companion object {
        internal const val HEADER_AUTH_KEY = "Authorization"
    }


    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val response = proceedRequest(chain, original)
        if (!response.isSuccessful) {
            val code = response.code()
            val errorBody = response.body().toString()
            val errorProcessor = errorProcessors[code]
            if (errorProcessor != null && errorProcessor.processRequestWithError(errorBody, code)) {
                return proceedRequest(chain, original)
            }
        }
        return response
    }


    private fun proceedRequest(chain: Interceptor.Chain, request: Request): Response {
        return chain.proceed(
            addAdditionalHeaders(addTokenInHeader(request))
                .build()
        )
    }

    private fun addTokenInHeader(request: Request): Request.Builder {
        val builder = request.newBuilder()
        if (authManager.isAuthenticated()) {
            val authType = authManager.getAuthType()
            if (authType != null)
                builder.addHeader(HEADER_AUTH_KEY, "$authType ${authManager.getToken()}")
            else
                builder.addHeader(HEADER_AUTH_KEY, authManager.getToken())
        } else {
            if (apiKey != null)
                builder.addHeader(HEADER_AUTH_KEY, apiKey)
        }
        return builder
    }

    /**
     * В этом методе необходимо добавить какие то дополнительные хедеры
     *
     * */
    abstract fun addAdditionalHeaders(builder: Request.Builder): Request.Builder
}