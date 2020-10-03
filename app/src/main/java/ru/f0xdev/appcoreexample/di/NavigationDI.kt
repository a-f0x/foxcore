package ru.f0xdev.appcoreexample.di

import org.koin.dsl.module
import ru.f0xdev.appcoreexample.routing.AppRouter
import ru.f0xdev.appcoreexample.routing.IAppRouter
import ru.f0xdev.f0xcore.base.navigation.routing.ActivityRouter
import ru.f0xdev.f0xcore.base.navigation.routing.IRouter
import ru.f0xdev.f0xcore.base.navigation.routing.LocalCiceroneHolder
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

val navigationModule = module {

    single<Cicerone<Router>> {
        Cicerone.create()
    }

    single<NavigatorHolder> {
        get<Cicerone<Router>>().navigatorHolder
    }

    single<Router> {
        val cicerone: Cicerone<Router> = get()
        cicerone.router
    }

    single {
        LocalCiceroneHolder()
    }


    single<IRouter> {
        ActivityRouter(
            router = get(),
            context = get()
        )
    }
    single<IAppRouter> {
        AppRouter(
            activityRouter = get()

        )

    }
}