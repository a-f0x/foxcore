package ru.f0xdev.f0xcore.base.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.base.ABaseFragment
import ru.f0xdev.f0xcore.base.IEmptyView


interface ListView<I> {
    fun setItems(items: List<I>)
    fun invalidateItem(item: I)
    fun addItem(item: I)
    fun removeItem(item: I)
}

abstract class ABaseRecyclerViewFragment<I, ADAPTER : ABaseRecyclerViewAdapter<I>> :
    ABaseFragment(), ListView<I> {

    abstract val fragmentLayoutId: Int

    abstract val adapter: ADAPTER

    abstract val swipeRefresh: SwipeRefreshLayout?

    abstract val emptyView: IEmptyView?

    abstract val recyclerView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(fragmentLayoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        swipeRefresh?.setColorSchemeResources(R.color.core_primary_color)
        swipeRefresh?.setOnRefreshListener {
            onLoadData(true)
        }
    }


    abstract fun onLoadData(bySwipeRefresh: Boolean)

    override fun showProgress(show: Boolean) {
        swipeRefresh?.let { sw ->
            if (sw.isRefreshing) {
                sw.isRefreshing = false
                return
            }
        }
        super.showProgress(show)
    }

    override fun setItems(items: List<I>) {
        adapter.bindData(items)
        updateEmptyViewVisibility()
    }

    override fun invalidateItem(item: I) {
        adapter.invalidateItem(item)
    }

    override fun addItem(item: I) {
        adapter.addItem(item)
    }

    override fun removeItem(item: I) {
        adapter.removeItem(item)
        updateEmptyViewVisibility()
    }

    private fun updateEmptyViewVisibility() {
        val itemCount = adapter.itemCount
        emptyView?.setVisible(itemCount == 0)
    }

}