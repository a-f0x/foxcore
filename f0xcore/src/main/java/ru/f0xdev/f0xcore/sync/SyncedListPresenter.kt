package ru.f0xdev.f0xcore.sync

import io.reactivex.functions.Consumer
import ru.f0xdev.f0xcore.analytic.IAnalytic
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.presentation.presenters.ABaseMixedPresenter
import ru.f0xdev.f0xcore.providers.ICoroutineContextProvider
import ru.f0xdev.f0xcore.providers.IRxSchedulerProvider

open class SyncedListPresenter<T : SyncedItem, V : SyncedListView<T>>(
    errorProcessor: IErrorProcessor,
    provider: ICoroutineContextProvider,
    rxSchedulerProvider: IRxSchedulerProvider,
    analytic: IAnalytic,
    private val repo: ISyncRepository<T>
) : ABaseMixedPresenter<V>(errorProcessor, rxSchedulerProvider, provider, analytic) {

    private var isSyncStarted = false

    private var itemForDelete: T? = null


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        launchOnUI(false,
            block = {
                val list = launchBackground { repo.getList() }.await()
                viewState.setItems(list)
            })

        repo.subject.executeInIOAndObserveUI(onNextConsumer = Consumer { event ->
            when (event) {
                is AddItemSyncEvent -> {
                    viewState.addItem(event.value)
                    launchBackground { repo.sendToRemote(event.value) }
                }

                is UpdateItemSyncEvent -> {
                    viewState.invalidateItem(event.value)
                }
                is DeleteItemSyncEvent -> {
                    viewState.removeItem(event.value)
                }
            }
        })
    }

    /**
     * Показать форму добавления нового итема в список
     * */
    open fun onAddItemClick() {
        viewState.showAddItemScreen()
    }

    /**
     * повторить отправку на сервер
     * **/
    open fun onRetryClickItem(item: T) {
        launchOnUI(false, block = {
            launchBackground { repo.sendToRemote(item) }
        })
    }

    /**
     * нажали на кнопку удаления из списка
     * */
    open fun onDeleteItemClick(item: T) {
        itemForDelete = item
        viewState.showDeleteItemConfirmDialog()
    }

    /**
     * отменили удаление из списка
     * */
    open fun cancelDeleteItem() {
        itemForDelete = null
    }

    /**
     * подтвердили удаление из списка
     * **/
    open fun deleteItem() {
        itemForDelete?.let {
            launchOnUI(false, block = {
                launchBackground { repo.delete(it) }
            })
        }
    }

    /***
     * Синхронизировать весь список с сервером
     * */

    open fun onSyncClick() {
        if (isSyncStarted)
            return
        isSyncStarted = true
        launchOnUI(true,
            block = {
                launchBackground {
                    repo.startSyncAll()
                }.await()
                isSyncStarted = false
                viewState.showProgress(false)
            }
        )
    }
}