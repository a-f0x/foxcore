package ru.f0xdev.f0xcore.presentation.presenters

import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


suspend fun <T> Single<T>.rxSingleToCoroutine(): T {
    return suspendCancellableCoroutine { coroutine ->
        this.subscribe({ coroutine.resume(it) }, { coroutine.resumeWithException(it) })
    }
}

suspend fun Completable.rxCompletableToCoroutine() {
    return suspendCancellableCoroutine { coroutine ->
        this.subscribe({ coroutine.resume(Unit) }, { coroutine.resumeWithException(it) })
    }
}