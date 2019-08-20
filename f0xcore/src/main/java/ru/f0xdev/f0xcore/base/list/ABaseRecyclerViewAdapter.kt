package ru.f0xdev.f0xcore.base.list

import androidx.recyclerview.widget.RecyclerView

abstract class ABaseRecyclerViewAdapter<I>(var onActionClick: ((item: I) -> Unit)? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val items = mutableListOf<I>()

    override fun getItemCount(): Int = items.size

    fun addItem(item: I) {
        if (items.contains(item))
            return
        items.add(item)
        notifyItemInserted(items.indexOf(item))
    }

    fun removeItem(item: I) {
        val index = getItemPosition(item)
        if (index == -1)
            return
        items.removeAt(index)
        notifyItemRemoved(index)
    }

    fun invalidateItem(item: I) {
        val position = getItemPosition(item)
        if (position != -1)
            notifyItemChanged(position)
    }

    fun bindData(list: List<I>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()

    }

    protected fun getItem(position: Int): I {
        return items[position]
    }

    private fun getItemPosition(item: I): Int = items.indexOf(item)

}