package ru.f0xdev.appcoreexample.main.chats

interface IChatsInteractor {
    suspend fun getChatList(): List<ChatListItem>
}


class ChatsInteractor : IChatsInteractor {
    override suspend fun getChatList(): List<ChatListItem> {
        return emptyList()
    }

}