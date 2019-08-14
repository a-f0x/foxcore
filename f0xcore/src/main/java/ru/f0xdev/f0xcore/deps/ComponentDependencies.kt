package ru.f0xdev.f0xcore.deps

import android.app.Activity
import android.app.Service
import androidx.fragment.app.Fragment

interface ComponentDependencies

typealias ComponentDependenciesProvider = Map<Class<out ComponentDependencies>, @JvmSuppressWildcards ComponentDependencies>

interface HasComponentDependencies {
    val dependencies: ComponentDependenciesProvider
}


inline fun <reified T : ComponentDependencies> Fragment.findComponentDependencies(): T {
    return findComponentDependenciesProvider()[T::class.java] as T
}

inline fun <reified T : ComponentDependencies> Activity.findComponentDependencies(): T {
    return findComponentDependenciesProvider()[T::class.java] as T
}

inline fun <reified T : ComponentDependencies> Service.findComponentDependencies(): T {
    return findComponentDependenciesProvider()[T::class.java] as T
}

inline fun <reified T : ComponentDependencies> ComponentDependenciesProvider.findDeps(clazz: Class<T>): T {
    val deps = this[clazz]
    if (deps != null)
        return deps as T
    else
        throw IllegalStateException("Can not find suitable dagger provider for $this")
}

fun Fragment.findComponentDependenciesProvider(): ComponentDependenciesProvider {
    var current: Fragment? = parentFragment
    while (current !is HasComponentDependencies?) {
        current = current?.parentFragment
    }

    val hasDaggerProviders = current ?: when {
        activity is HasComponentDependencies -> activity as HasComponentDependencies
        activity?.application is HasComponentDependencies -> activity?.application as HasComponentDependencies
        else -> throw IllegalStateException("Can not find suitable dagger provider for $this")
    }
    return hasDaggerProviders.dependencies
}

fun Activity.findComponentDependenciesProvider(): ComponentDependenciesProvider {

    if (this is HasComponentDependencies)
        return this.dependencies

    if (application is HasComponentDependencies)
        return (application as HasComponentDependencies).dependencies

    throw IllegalStateException("Can not find suitable dagger provider for $this")
}

fun Service.findComponentDependenciesProvider(): ComponentDependenciesProvider {
    if (this is HasComponentDependencies)
        return this.dependencies

    if (application is HasComponentDependencies)
        return (application as HasComponentDependencies).dependencies

    throw IllegalStateException("Can not find suitable dagger provider for $this")
}

