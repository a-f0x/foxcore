package ru.f0xdev.f0xcore.net

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

abstract class ABaseHeaderInterceptor(
    var errorProcessors: Map<Int, IResponseErrorProcessor>
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val response = proceedRequest(chain, original)
        if (!response.isSuccessful) {
            val code = response.code
            val errorBody = response.body.toString()
            val errorProcessor = errorProcessors[code]
            if (errorProcessor != null && errorProcessor.processRequestWithError(errorBody, code)) {
                return proceedRequest(chain, original)
            }
        }
        return response
    }

    private fun proceedRequest(chain: Interceptor.Chain, request: Request): Response {
        return chain.proceed(
            addAdditionalHeaders(request.newBuilder())
                .build()
        )
    }

    /**
     * В этом методе возможно добавление дополнительных хедереров
     *
     * */
    abstract fun addAdditionalHeaders(builder: Request.Builder): Request.Builder
}