package ru.f0xdev.f0xcore.base.list

import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType


@StateStrategyType(OneExecutionStateStrategy::class)
interface ListView<I> {
    fun setItems(items: List<I>)
    fun invalidateItem(item: I)
    fun addItem(item: I)
    fun removeItem(item: I)
}
