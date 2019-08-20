package ru.f0xdev.appcoreexample.main.users

import com.arellomobile.mvp.InjectViewState
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.presentation.presenters.ABaseCoroutinePresenter
import ru.f0xdev.f0xcore.providers.ICoroutineContextProvider

@InjectViewState
class UsersListFragmentPresenter(
    private val userInteractor: IUsersInteractor,
    errorProcessor: IErrorProcessor,
    provider: ICoroutineContextProvider
) : ABaseCoroutinePresenter<UsersListView>(errorProcessor, provider) {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadUsers(false)
    }

    fun loadUsers(bySwipeRefresh: Boolean) {
        launchOnUI(showProgress = !bySwipeRefresh,
            block = {
                val users = launchBackground { userInteractor.loadUsers() }.await()
                viewState.showProgress(false)
                viewState.setItems(users)
            })
    }

    fun onUserClick(user: UsersListItem) {


    }
}