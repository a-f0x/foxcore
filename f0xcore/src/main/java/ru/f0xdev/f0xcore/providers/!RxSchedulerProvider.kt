package ru.f0xdev.f0xcore.providers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


interface IRxSchedulerProvider {

    val computation: Scheduler

    val io: Scheduler

    val ui: Scheduler
}

class DefaultRxSchedulerProvider : IRxSchedulerProvider {
    override val computation: Scheduler = Schedulers.computation()
    override val io: Scheduler = Schedulers.io()
    override val ui: Scheduler = AndroidSchedulers.mainThread()
}

class TestRxSchedulerProvider : IRxSchedulerProvider {
    override val computation: Scheduler = Schedulers.trampoline()
    override val io: Scheduler = Schedulers.trampoline()
    override val ui: Scheduler = Schedulers.trampoline()
}

