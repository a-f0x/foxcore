package ru.f0xdev.appcoreexample.presentation.main.users

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.f0xdev.f0xcore.base.IEmptyView
import ru.f0xdev.f0xcore.base.list.ABaseRecyclerViewFragment


class UsersListItem


class UsersListFragment : ABaseRecyclerViewFragment<UsersListItem, UsersListAdapter>() {


    override val fragmentLayoutId: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val adapter: UsersListAdapter
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val swipeRefresh: SwipeRefreshLayout?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val emptyView: IEmptyView?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val recyclerView: RecyclerView
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun onLoadData(bySwipeRefresh: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}