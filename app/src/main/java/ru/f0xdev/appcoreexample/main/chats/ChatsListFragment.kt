package ru.f0xdev.appcoreexample.main.chats

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_chats.*
import org.koin.android.ext.android.inject
import ru.f0xdev.appcoreexample.R
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.base.IEmptyView
import ru.f0xdev.f0xcore.base.list.ABaseRecyclerViewFragment
import ru.f0xdev.f0xcore.base.list.ListView


class ChatListItem(
    var title: String,
    var lastMessage: String,
    val status: String,
    val id: Int
)

interface ChatsListView : BaseView, ListView<ChatListItem>


class ChatsListFragment : ABaseRecyclerViewFragment<ChatListItem, ChatsListAdapter>(), ChatsListView {
    private val p: ChatsListFragmentPresenter by inject()

    @InjectPresenter
    lateinit var presenter: ChatsListFragmentPresenter

    @ProvidePresenter
    fun provide() = p

    override val fragmentLayoutId: Int = R.layout.fragment_chats

    override val adapter: ChatsListAdapter = ChatsListAdapter { chat ->
        presenter.onChatClick(chat)
    }

    override val swipeRefresh: SwipeRefreshLayout = chatsSwipeRefresh
    override val emptyView: IEmptyView? = chatsEmptyView

    override val recyclerView: RecyclerView = chatsRecyclerView


    override fun onLoadData(bySwipeRefresh: Boolean) {
        presenter.loadChats(bySwipeRefresh)
    }


}