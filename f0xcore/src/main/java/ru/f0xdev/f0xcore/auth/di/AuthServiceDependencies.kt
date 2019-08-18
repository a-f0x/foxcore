package ru.f0xdev.f0xcore.auth.di

import ru.f0xdev.f0xcore.auth.ITokenCryptographer
import ru.f0xdev.f0xcore.auth.net.IAuthRemoteDataSource
import ru.f0xdev.f0xcore.deps.ComponentDependencies


interface AuthServiceDependencies : ComponentDependencies {
    fun provideAuthRemoteDataSource(): IAuthRemoteDataSource
    fun provideITokenCryptographer(): ITokenCryptographer
}