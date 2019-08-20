package ru.f0xdev.appcoreexample.main.chats

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.f0xdev.appcoreexample.main.users.UsersListAdapter
import ru.f0xdev.f0xcore.base.list.ABaseRecyclerViewAdapter

class ChatsListAdapter(onActionClick: ((item: ChatListItem) -> Unit)? = null) :
    ABaseRecyclerViewAdapter<ChatListItem>(onActionClick) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UsersListAdapter.ChatViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }
}