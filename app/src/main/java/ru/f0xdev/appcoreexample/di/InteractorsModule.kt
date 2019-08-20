package ru.f0xdev.appcoreexample.di

import org.koin.dsl.module
import ru.f0xdev.appcoreexample.main.chats.ChatsInteractor
import ru.f0xdev.appcoreexample.main.chats.IChatsInteractor
import ru.f0xdev.appcoreexample.main.users.IUsersInteractor
import ru.f0xdev.appcoreexample.main.users.UsersInteractor

val interactrorModule = module {
    factory<IUsersInteractor> {
        UsersInteractor(get())
    }
    factory<IChatsInteractor> {
        ChatsInteractor()
    }
}
