package ru.f0xdev.f0xcore.base.navigation

import ru.f0xdev.f0xcore.base.ABaseFragment
import ru.f0xdev.f0xcore.base.BackPressableView
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppNavigator

abstract class ABaseNavigationFragment : ABaseFragment(), IContainerFragment {

    private var navigator: Navigator? = null

    abstract val navigationLifeCycle: NavigationLifeCycle

    override fun onResume() {
        super.onResume()
        navigationLifeCycle.onAttachNavigator(getNavigator())
    }

    override fun onPause() {
        navigationLifeCycle.detachNavigator()
        super.onPause()
    }

    fun isContainerEmpty(): Boolean {
        return childFragmentManager.findFragmentById(getContainerId()) == null
    }


    private fun getNavigator(): Navigator {
        return navigator ?: SupportAppNavigator(
            activity!!,
            childFragmentManager,
            getContainerId()
        )
    }

    override fun onBackPressed(): Boolean {
        val availableBackPressableView = childFragmentManager.fragments
            .lastOrNull { it.isAdded } as? BackPressableView

        val result = availableBackPressableView?.onBackPressed() ?: false

        if (result)
            return true

        if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager.popBackStack()
            return true
        }
        return false
    }

}