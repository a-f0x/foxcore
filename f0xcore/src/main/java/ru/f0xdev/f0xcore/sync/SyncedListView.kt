package ru.f0xdev.f0xcore.sync

import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.base.list.ListView


interface SyncedListView<T : SyncedItem> : BaseView, ListView<T> {
    @StateStrategyType(SkipStrategy::class)
    fun showDeleteItemConfirmDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showAddItemScreen()

}