package ru.f0xdev.f0xcore.providers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.CoroutineContext

interface ICoroutineContextProvider {
    val main: CoroutineContext
    val async: CoroutineContext
    /**
     * нужен для перехвата эксепшенов в уже отмененных корутинах
     * иначе падает
     * */
    val handler: CoroutineExceptionHandler

}

class DefaultCoroutineContextProvider : ICoroutineContextProvider {
    override val handler: CoroutineExceptionHandler = CoroutineExceptionHandler(handler = { _, error ->
        error.printStackTrace()
    })

    override val main: CoroutineContext = Dispatchers.Main + handler

    override val async: CoroutineContext = Dispatchers.IO + handler
}


@ExperimentalCoroutinesApi
class TestCoroutineContextProvider : ICoroutineContextProvider {
    override val main: CoroutineContext = Dispatchers.Unconfined
    override val async: CoroutineContext = Dispatchers.Unconfined
    override val handler: CoroutineExceptionHandler = CoroutineExceptionHandler(handler = { _, error ->
        error.printStackTrace()
    })
}


