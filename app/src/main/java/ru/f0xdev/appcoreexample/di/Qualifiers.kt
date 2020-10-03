package ru.f0xdev.appcoreexample.di

import org.koin.core.qualifier.named

val defaultIErrorProcessorQualifier = named("Default_error_processor")

val activityRouterQualifier = named("activity_router")
val fragmentRouterQualifier = named("fragment_router")