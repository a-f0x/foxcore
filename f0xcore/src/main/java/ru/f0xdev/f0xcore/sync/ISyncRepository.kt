package ru.f0xdev.f0xcore.sync

import io.reactivex.subjects.BehaviorSubject
import ru.f0xdev.f0xcore.base.crud.ICrudLocalStorage


sealed class SyncEvent<T : SyncedItem>(val value: T)

class AddItemSyncEvent<T : SyncedItem>(value: T) : SyncEvent<T>(value)
class DeleteItemSyncEvent<T : SyncedItem>(value: T) : SyncEvent<T>(value)

class UpdateItemSyncEvent<T : SyncedItem>(value: T, val error: Throwable? = null) :
    SyncEvent<T>(value)


interface ISyncRepository<T : SyncedItem> {
    suspend fun add(item: T)
    suspend fun delete(item: T)
    suspend fun sendToRemote(item: T)
    suspend fun startSyncAll()
    suspend fun getList(): List<T>
    val subject: BehaviorSubject<SyncEvent<T>>
}

open class SyncRepository<T : SyncedItem>(
    override val subject: BehaviorSubject<SyncEvent<T>>,
    private val local: ICrudLocalStorage<T>,
    private val remote: ISyncRemoteDataSource<T>
) : ISyncRepository<T> {


    override suspend fun add(item: T) {
        local.save(item)
        subject.onNext(AddItemSyncEvent(item))
    }

    override suspend fun delete(item: T) {
        local.delete(item)
        subject.onNext(DeleteItemSyncEvent(item))
    }

    override suspend fun sendToRemote(item: T) {
        item.status = SyncedItem.Status.creatySyncProcessStatus()
        local.save(item)
        subject.onNext(UpdateItemSyncEvent(item))
        try {
            Thread.sleep(2500)
            throw RuntimeException("1234")
//            remote.syncItem(item)
            item.status = SyncedItem.Status.createSyncSuccess()
            local.save(item)
            subject.onNext(UpdateItemSyncEvent(item))
        } catch (t: Throwable) {
            item.status = SyncedItem.Status.createSyncErrorStatus()
            local.save(item)
            subject.onNext(UpdateItemSyncEvent(item, t))
        }
    }

    override suspend fun startSyncAll() {
        val list = local.getList().filter {
            it.status.type == SyncedItem.Status.SYNC_ERROR
                    || it.status.type == SyncedItem.Status.NOT_SYNCED
        }
        list.forEach {
            sendToRemote(it)
        }
    }

    override suspend fun getList(): List<T> = local.getList()
}