package ru.f0xdev.appcoreexample.main.users

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_users.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.inject
import ru.f0xdev.appcoreexample.R
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.base.IEmptyView
import ru.f0xdev.f0xcore.base.list.ABaseRecyclerViewFragment
import ru.f0xdev.f0xcore.base.list.ListView


interface UsersListView : BaseView, ListView<UsersListItem>

class UsersListFragment : ABaseRecyclerViewFragment<UsersListItem, UsersListAdapter>(), UsersListView {

    private val p: UsersListFragmentPresenter by inject()

    @InjectPresenter
    lateinit var presenter: UsersListFragmentPresenter

    @ProvidePresenter
    fun provide() = p


    override val fragmentLayoutId: Int = R.layout.fragment_users


    override val adapter: UsersListAdapter = UsersListAdapter { user ->
        presenter.onUserClick(user)
    }

    override var swipeRefresh: SwipeRefreshLayout? = null
    override var emptyView: IEmptyView? = null
    override lateinit var recyclerView: RecyclerView

    override fun initViews() {
        swipeRefresh = usersSwipeRefresh
        emptyView = usersEmptyView
        recyclerView = usersRecyclerView
        progressLayout = usersProgressBar
    }


    override fun onLoadData(bySwipeRefresh: Boolean) {
        presenter.loadUsers(bySwipeRefresh)
    }


}