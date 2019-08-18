package ru.f0xdev.appcoreexample.di

import org.koin.dsl.module
import ru.f0xdev.f0xcore.auth.di.AuthServiceDependencies
import ru.f0xdev.f0xcore.deps.ComponentDependencies
import ru.f0xdev.f0xcore.deps.ComponentDependenciesProvider

val dependenciesProviderModule = module {
    single<ComponentDependenciesProvider>(createdAtStart = true) {
        val map: MutableMap<Class<out ComponentDependencies>, @JvmSuppressWildcards ComponentDependencies> =
            HashMap()
        map[AuthServiceDependencies::class.java] = get<AuthServiceDependencies>()
        map
    }
}

fun accountsModule(baseUrl: String) = module {
    single<AuthServiceDependencies> {
        AuthServiceDependenciesKoin(
            get(),
            get(),
            baseUrl
        )
    }


}
