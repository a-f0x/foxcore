package ru.f0xdev.f0xcore.rx

import io.reactivex.subjects.PublishSubject

/**
 * Простая реализация EventBus
 */
class RxEventBusPublish {
    private val bus = PublishSubject.create<RxEvent>()

    fun send(rxEvent: RxEvent) {
        bus.onNext(rxEvent)
    }

    fun toObservable(): PublishSubject<RxEvent> = bus
}