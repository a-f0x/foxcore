package ru.f0xdev.appcoreexample.main.chats

import com.arellomobile.mvp.InjectViewState
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.presentation.presenters.ABaseCoroutinePresenter
import ru.f0xdev.f0xcore.providers.ICoroutineContextProvider

@InjectViewState
class ChatsListFragmentPresenter(
    private val chatsInteractor: IChatsInteractor,
    errorProcessor: IErrorProcessor,
    provider: ICoroutineContextProvider
) : ABaseCoroutinePresenter<ChatsListView>(errorProcessor, provider) {

    override fun onFirstViewAttach() {
        loadChats(false)
    }

    fun loadChats(bySwipeRefresh: Boolean) {
        launchOnUI(
            showProgress = !bySwipeRefresh,
            block = {
                val chats = launchBackground { chatsInteractor.getChatList() }.await()
                viewState.showProgress(false)
                viewState.setItems(chats)
            }
        )
    }


    fun onChatClick(chat: ChatListItem) {

    }


}