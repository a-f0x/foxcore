package ru.f0xdev.appcoreexample.di

import org.koin.dsl.module
import ru.f0xdev.appcoreexample.main.settings.IProfileRepository
import ru.f0xdev.appcoreexample.main.settings.ProfileRepository
import ru.f0xdev.appcoreexample.main.users.IUsersRepository
import ru.f0xdev.appcoreexample.main.users.UserRepository

val repositoryModule = module {

    single<IUsersRepository> {
        UserRepository(
            get()
        )
    }

    single<IProfileRepository> {
        ProfileRepository()
    }
}