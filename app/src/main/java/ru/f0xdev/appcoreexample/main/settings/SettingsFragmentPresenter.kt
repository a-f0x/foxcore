package ru.f0xdev.appcoreexample.main.settings

import com.arellomobile.mvp.InjectViewState
import ru.f0xdev.f0xcore.auth.IAuthManager
import ru.f0xdev.f0xcore.presentation.errors.IErrorProcessor
import ru.f0xdev.f0xcore.presentation.presenters.ABaseCoroutinePresenter
import ru.f0xdev.f0xcore.providers.ICoroutineContextProvider

@InjectViewState
class SettingsFragmentPresenter(
    private val profileRepository: IProfileRepository,
    private val authManager: IAuthManager,
    errorProcessor: IErrorProcessor,
    provider: ICoroutineContextProvider
) : ABaseCoroutinePresenter<SettingsView>(errorProcessor, provider) {


    override fun onFirstViewAttach() {
        loadProfile()
    }

    private fun loadProfile() {
        launchOnUI(block = {
            val profile = launchBackground { profileRepository.loadProfile() }.await()
            viewState.showProgress(false)
            viewState.setUserProfile(profile)
        })
    }


    fun onAboutClick() {

    }

    fun onLogoutClick() {
        viewState.showConfirmationDialog()
    }

    fun logout() {
        launchOnUI(false, block = {
            launchBackground { authManager.logout() }.await()
        })
    }
}