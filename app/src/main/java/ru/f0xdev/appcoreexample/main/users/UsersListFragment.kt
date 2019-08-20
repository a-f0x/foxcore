package ru.f0xdev.appcoreexample.main.users

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_users.*
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

    override val progressLayout: View? = usersProgressBar

    override val adapter: UsersListAdapter = UsersListAdapter { user ->
        presenter.onUserClick(user)
    }

    override val swipeRefresh: SwipeRefreshLayout = usersSwipeRefresh

    override val emptyView: IEmptyView? = usersEmptyView

    override val recyclerView: RecyclerView = usersRecyclerView


    override fun onLoadData(bySwipeRefresh: Boolean) {
        presenter.loadUsers(bySwipeRefresh)
    }


}