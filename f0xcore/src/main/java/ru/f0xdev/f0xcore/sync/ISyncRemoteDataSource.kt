package ru.f0xdev.f0xcore.sync

interface ISyncRemoteDataSource<T> {
    suspend fun syncItem(item: T)
}