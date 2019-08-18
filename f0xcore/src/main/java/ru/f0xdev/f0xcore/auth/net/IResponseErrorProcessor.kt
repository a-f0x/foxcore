package ru.f0xdev.f0xcore.auth.net

import java.io.IOException


interface IResponseErrorProcessor {
    /**
     * @return true то запрос на сервер который сфейлился будет перевыполнент
     * */
    @Throws(IOException::class)
    fun processRequestWithError(errorBody: String?, errorCode: Int): Boolean
}


