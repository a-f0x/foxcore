package ru.f0xdev.f0xcore.base.crud

import ru.f0xdev.f0xcore.base.cache.Cache


interface ICrudLocalStorage<T> {
    fun save(item: T): T

    fun getList(): List<T>

    fun delete(item: T)
}

abstract class CrudFileLocalStorage<T>(protected val cache: Cache) : ICrudLocalStorage<T> {

    @Synchronized
    override fun save(item: T): T {
        val holder = getOrCreate()
        val index = holder.list.indexOf(item)
        if (index == -1)
            holder.list.add(item)
        else
            holder.list[index] = item

        cache.write(holder)
        return item

    }

    @Synchronized
    override fun getList(): List<T> {
        return getOrCreate().list
    }

    @Synchronized
    override fun delete(item: T) {
        val holder = getOrCreate()
        if (holder.list.remove(item))
            cache.write(holder)
    }


    abstract fun getOrCreate(): ListHolder<T>


    interface ListHolder<T> {
        val list: MutableList<T>
    }
}