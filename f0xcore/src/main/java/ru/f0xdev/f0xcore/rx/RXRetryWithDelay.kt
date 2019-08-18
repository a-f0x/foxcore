package ru.f0xdev.f0xcore.rx

import io.reactivex.Flowable
import io.reactivex.functions.Function
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

class RXRetryWithDelay(
    private val maxRetries: Int,
    private val retryDelayMillis: Long

) : Function<Flowable<Throwable>, Publisher<*>> {
    private var retryCount: Int = 0

    companion object {
        private const val TAG = "RXRetryWithDelay"
        const val DEFAULT_RETRY = 5
        const val DEFAULT_DELAY = 3000L
    }

    override fun apply(t: Flowable<Throwable>): Publisher<*> {
        return t.flatMap {
            if (++retryCount < maxRetries) {
                return@flatMap Flowable.timer(
                    retryDelayMillis,
                    TimeUnit.MILLISECONDS
                )
            }
            return@flatMap Flowable.error<Throwable>(it)
        }
    }

}