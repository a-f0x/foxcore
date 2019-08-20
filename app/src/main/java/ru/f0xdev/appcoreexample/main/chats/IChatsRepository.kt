package ru.f0xdev.appcoreexample.main.chats

interface IChatsRepository {
    suspend fun loadChats()
}