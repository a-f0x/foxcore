package ru.f0xdev.appcoreexample.main.users

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.f0xdev.appcoreexample.R
import ru.f0xdev.appcoreexample.util.PicassoImageCirleTransformation
import ru.f0xdev.f0xcore.base.list.ABaseRecyclerViewAdapter
import ru.f0xdev.f0xcore.util.getInflater

class UsersListAdapter(onActionClick: ((item: UsersListItem) -> Unit)? = null) :
    ABaseRecyclerViewAdapter<UsersListItem>(onActionClick) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatViewHolder(parent)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is ChatViewHolder)
            return
        val item = getItem(position)
        Picasso.get()
            .load(item.avatar)
            .transform(PicassoImageCirleTransformation())
            .into(holder.ivAvatar)
        holder.tvName.text = "${item.firstName} ${item.firstName}"
        holder.tvStatus.text = item.status
    }


    class ChatViewHolder(
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(parent.context.getInflater().inflate(R.layout.item_chat_view, parent, false)) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }
}