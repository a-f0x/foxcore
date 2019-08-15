package ru.f0xdev.f0xcore.base.cache

interface Cache {

    fun write(any: Any)

    fun <T> read(clazz: Class<T>): T?

    fun <T> clear(clazz: Class<T>)

    fun clearAll()
}