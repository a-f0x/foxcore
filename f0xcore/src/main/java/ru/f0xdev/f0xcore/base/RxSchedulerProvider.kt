package ru.f0xdev.f0xcore.base

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


interface RxSchedulerProvider {

    fun computation(): Scheduler = Schedulers.computation()

    fun io(): Scheduler = Schedulers.io()

    fun ui(): Scheduler = AndroidSchedulers.mainThread()
}

class DefaultRxSchedulerProvider : RxSchedulerProvider

