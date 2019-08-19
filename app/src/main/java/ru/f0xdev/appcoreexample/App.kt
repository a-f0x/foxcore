package ru.f0xdev.appcoreexample

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import ru.f0xdev.appcoreexample.di.*
import ru.f0xdev.f0xcore.deps.ComponentDependenciesProvider
import ru.f0xdev.f0xcore.deps.HasComponentDependencies

class App : MultiDexApplication(), HasComponentDependencies, KoinComponent {


    override val dependencies: ComponentDependenciesProvider by lazy {
        getKoin().get<ComponentDependenciesProvider>()
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    commonModule,
                    networkModule("https://reqres.in"),
                    accountsModule("https://reqres.in"),
                    dependenciesProviderModule,
                    presentersModule

                )
            )
        }
    }


}