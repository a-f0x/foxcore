package ru.f0xdev.f0xcore.base

/**
 * Делегат обработки кнопки back
 *
 * */
interface BackPressableView {
    /**
     * @return true - вьюха сама обработала сценарий бека и вышестояшей вью (активити или парент фрагмент) не нужно вызывать суперовый метд
     * false - ну тут все наоборот, делегируем вышестоящей вью обработку бека
     * */
    fun onBackPressed(): Boolean
}