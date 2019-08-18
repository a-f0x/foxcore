package ru.f0xdev.f0xcore.presentation.presenters

import kotlinx.coroutines.Job

class CompositeJob {
    private val map = hashMapOf<String, Job>()
    /**
     * добавить джобу для хранения
     * @param job джоба которую возвращает корутин биллер [kotlinx.coroutines.CoroutineScope]
     * @param key ключ по которому ее можно будет достать из коллекции если надо
     * */
    fun add(job: Job, key: String = job.hashCode().toString()) = map.put(key, job)?.cancel()

    fun cancel(key: String) = map[key]?.cancel()

    fun cancel() = map.values.forEach { it.cancel() }
}
