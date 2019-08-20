package ru.f0xdev.appcoreexample.presentation.main.chats

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.f0xdev.f0xcore.base.list.ABaseRecyclerViewAdapter

class ChatsListAdapter(onActionClick: ((item: ChatListItemView) -> Unit)? = null) :
    ABaseRecyclerViewAdapter<ChatListItemView>(onActionClick) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}