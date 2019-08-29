package ru.f0xdev.f0xcore.sync

import ru.f0xdev.f0xcore.base.cache.Cache
import ru.f0xdev.f0xcore.base.crud.CrudFileLocalStorage
import ru.f0xdev.f0xcore.base.crud.ICrudLocalStorage


abstract class CrudFileLocalStorageSyncedItem<T : SyncedItem>(cache: Cache) :
    CrudFileLocalStorage<T>(cache), ICrudLocalStorage<T> {

    @Synchronized
    override fun save(item: T): T {
        val holder = getOrCreate()
        val oldIndex = holder.list.indexOf(item)

        if (oldIndex != -1)
            holder.list[oldIndex] = item
        else {
            val nexId = getNextIndex(holder.list)
            item.apply { id = nexId }

            holder.list.add(item)
        }
        cache.write(holder)
        return item

    }


    private fun getNextIndex(list: List<T>): Int {
        if (list.isEmpty())
            return 0
        return list.maxBy { it.id }!!.id + 1
    }
}