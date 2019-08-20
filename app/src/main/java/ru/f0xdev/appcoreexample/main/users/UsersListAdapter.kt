package ru.f0xdev.appcoreexample.main.users

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.f0xdev.f0xcore.base.list.ABaseRecyclerViewAdapter

class UsersListAdapter(onActionClick: ((item: UsersListItem) -> Unit)? = null) :
    ABaseRecyclerViewAdapter<UsersListItem>(onActionClick) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}