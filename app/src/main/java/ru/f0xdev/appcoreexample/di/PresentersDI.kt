package ru.f0xdev.appcoreexample.di

import org.koin.dsl.module
import ru.f0xdev.appcoreexample.auth.AuthErrorMapper
import ru.f0xdev.appcoreexample.auth.AuthPresenter
import ru.f0xdev.appcoreexample.errors.BaseErrorMapper
import ru.f0xdev.appcoreexample.launch.LaunchPresenter
import ru.f0xdev.appcoreexample.main.chats.ChatsListFragmentPresenter
import ru.f0xdev.appcoreexample.main.settings.SettingsFragmentPresenter
import ru.f0xdev.appcoreexample.main.users.UsersListFragmentPresenter
import ru.f0xdev.f0xcore.presentation.errors.ErrorProcessor
import ru.f0xdev.f0xcore.presentation.errors.ErrorViewDispatcher
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor

val presentersModule = module {
    factory<IErrorProcessor>(qualifier = defaultIErrorProcessorQualifier) {
        ErrorProcessor(
            BaseErrorMapper(get()),
            ErrorViewDispatcher()
        )
    }

    factory {
        LaunchPresenter(
            get(defaultIErrorProcessorQualifier),
            get(),
            get()
        )
    }

    factory {
        AuthPresenter(
            ErrorProcessor(
                AuthErrorMapper(get()),
                ErrorViewDispatcher()

            ),
            get(),
            get()
        )
    }

    factory {
        UsersListFragmentPresenter(
            get(),
            get(defaultIErrorProcessorQualifier),
            get()
        )
    }

    factory {
        ChatsListFragmentPresenter(
            get(),
            get(defaultIErrorProcessorQualifier),
            get()
        )
    }
    factory {
        SettingsFragmentPresenter(
            get(),
            get(),
            get(defaultIErrorProcessorQualifier),
            get()
        )
    }
}