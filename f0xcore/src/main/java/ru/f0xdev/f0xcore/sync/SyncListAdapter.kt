package ru.f0xdev.f0xcore.sync

import ru.f0xdev.f0xcore.base.list.ABaseRecyclerViewAdapter

abstract class SyncListAdapter<T : SyncedItem>(
    onActionClick: ((item: T) -> Unit)? = null

) : ABaseRecyclerViewAdapter<T>(onActionClick) {

    override fun bindData(list: List<T>) {
        super.bindData(list.sortedBy { it.status.type })
    }

    override fun addItem(item: T) {
        if (items.contains(item))
            return
        items.add(0, item)
        notifyItemInserted(0)
    }

    override fun invalidateItem(item: T) {
        items.firstOrNull { item == it }?.let {
            it.status = item.status
        }
        if (item.status.type == SyncedItem.Status.SYNC_SUCCESS) {
            removeItem(item)
            val firstIndexOfSyncedItems =
                items.indexOfFirst { it.status.type == SyncedItem.Status.SYNC_SUCCESS }

            if (firstIndexOfSyncedItems != -1) {
                items.add(firstIndexOfSyncedItems, item)
                notifyItemInserted(firstIndexOfSyncedItems)
            } else {

                items.add(item)
                notifyItemInserted(items.size - 1)
            }
            return
        }

        super.invalidateItem(item)
    }
}