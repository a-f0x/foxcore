package ru.f0xdev.f0xcore.base.navigation.routing

import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen


interface IRouter {

    /**
     * Добавить экран к текущему стеку экранов
     * возможность вернуться на предыдущий экран есть
     * */
    fun navigateTo(screen: SupportAppScreen)

    /**
     * Заменить текущиий (последний) экран на другой с сохранением предыдущего стека
     * например был стек: 1-2-3
     * стал 1-2-4
     *
     * */
    fun replaceScreen(screen: SupportAppScreen)

    /**
     * Заменить текущий стек на новый, заменяет весь текущий стек на экран который передали в
     * @param screen
     * */
    fun rootScreen(screen: SupportAppScreen)

    /**
     * Вернуться на экран назад
     *
     * */
    fun back()

    /**
     * вернутся на конкретный экран
     * */
    fun backTo(screen: SupportAppScreen)

    /**
     * добавляет новый стек экранов к текущему
     * */
    fun newChain(screens: Array<Screen>)

    /**
     * Устанавливает новый рутовый стек экранов по аналогии с [rootScreen]
     *
     * */
    fun newRootChain(screens: Array<Screen>)

    /**
     * Завершить текущую цепочку экранов, как правило используется для выхода из приложения
     * */
    fun finishChain()
}