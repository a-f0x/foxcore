package ru.f0xdev.f0xcore.rx

import io.reactivex.subjects.BehaviorSubject

class RxEventBusBehavior {

    private val bus = BehaviorSubject.create<RxEvent>()

    fun send(rxEvent: RxEvent) {
        bus.onNext(rxEvent)
    }

    fun toObservable(): BehaviorSubject<RxEvent> = bus

}