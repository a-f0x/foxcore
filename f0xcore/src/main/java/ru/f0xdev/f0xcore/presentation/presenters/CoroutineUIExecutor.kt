package ru.f0xdev.f0xcore.presentation.presenters

import kotlinx.coroutines.*
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.presentation.errors.IError
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.providers.ICoroutineContextProvider

interface CoroutineUIExecutor<View : BaseView> {
    val view: View
    val errorProcessor: IErrorProcessor
    val compositeJob: CompositeJob
    val provider: ICoroutineContextProvider

    /**
     * сохранить джобу (сбилженную корутину)
     * что бы при детаче вью не забыть ее отменить.
     * иначе будет либо мемори лик либо падение
     * @param job - сбилженная корутина
     * @param taskKey - ключ по которому ее можно будет достать если нужно
     * */

    private fun addJob(job: Job, taskKey: String? = null) {
        taskKey?.let {
            compositeJob.add(job, it)
            return
        }
        compositeJob.add(job)
    }

    /**
     * отменить джобу по конкретному ключу
     * @param taskKey ключ который был передан в [addJob]
     * */
    fun cancelJob(taskKey: String) {
        compositeJob.cancel(taskKey)
    }

    /**
     * отменить все джобы разом
     * */
    fun cancelAllJobs() {
        compositeJob.cancel()
    }

    /**
     *выполнить корутину и вернуть результат в UI
     * */
    fun launchOnUI(
        showProgress: Boolean = true,
        taskKey: String? = null,
        block: suspend () -> Unit,
        onError: (error: IError) -> Unit = { _: IError -> },
        retryAction: (() -> Unit)? = null

    ): Job {
        val job = GlobalScope.launch(provider.main) {
            if (showProgress) view.showProgress(true)
            try {
                block.invoke()
            } catch (ex: Throwable) {
                errorProcessor.processExceptionOnView(ex, view, onError, retryAction)
            }
        }
        addJob(job, taskKey)
        return job
    }

    /**
     *выполнить корутину в другом потоке
     * */
    fun <T> launchBackground(
        taskKey: String? = null,
        block: suspend () -> T
    ): Deferred<T> {
        val job = GlobalScope.async(provider.async) {
            block.invoke()
        }
        addJob(job, taskKey)
        return job
    }

    fun release() {
        compositeJob.cancel()
    }

}