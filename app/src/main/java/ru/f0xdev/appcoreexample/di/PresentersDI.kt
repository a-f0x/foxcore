package ru.f0xdev.appcoreexample.di

import org.koin.dsl.module
import ru.f0xdev.appcoreexample.presentation.BaseErrorMapper
import ru.f0xdev.appcoreexample.presentation.auth.AuthErrorMapper
import ru.f0xdev.appcoreexample.presentation.auth.AuthPresenter
import ru.f0xdev.appcoreexample.presentation.launch.LaunchPresenter
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
}