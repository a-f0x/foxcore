package ru.f0xdev.f0xcore.base

import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import ru.f0xdev.f0xcore.R

interface IFragmentActivity {

    val fragmentContainerId: Int

    fun switchFragmentWithCustomAnimation(
        fragment: Fragment,
        tag: String?,
        addToBackStack: Boolean = false,
        activity: FragmentActivity,
        clearTopFragments: Boolean = false,
        @AnimatorRes enterAnimator: Int = R.animator.animator_fragment_fade_in,
        @AnimatorRes exitAnimator: Int = R.animator.animator_fragment_fade_out
    ) {
        val fragmentTransaction = activity.supportFragmentManager
            .beginTransaction()
        fragmentTransaction.setCustomAnimations(enterAnimator, exitAnimator)
        executeTransaction(fragmentTransaction, activity, fragment, tag, clearTopFragments, addToBackStack)
    }

    fun switchFragmentWithDefaultAnimation(
        fragment: Fragment,
        tag: String?,
        addToBackStack: Boolean = false,
        activity: FragmentActivity,
        clearTopFragments: Boolean = false
    ) {
        val fragmentTransaction = activity.supportFragmentManager
            .beginTransaction()
        executeTransaction(
            fragmentTransaction,
            activity,
            fragment,
            tag,
            clearTopFragments,
            addToBackStack
        )
    }

    fun switchFragmentWithoutAnimation(
        fragment: Fragment,
        tag: String?,
        addToBackStack: Boolean = false,
        activity: FragmentActivity,
        clearTopFragments: Boolean = false
    ) {
        val fragmentTransaction = activity.supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_NONE)
        executeTransaction(
            fragmentTransaction,
            activity,
            fragment,
            tag,
            clearTopFragments,
            addToBackStack
        )
    }

    private fun executeTransaction(
        transaction: FragmentTransaction,
        activity: FragmentActivity,
        fragment: Fragment,
        tag: String?,
        clearTopFragments: Boolean,
        addToBackStack: Boolean
    ) {
        if (clearTopFragments) {
            val fm = activity.supportFragmentManager
            for (i in 0 until fm.backStackEntryCount) {
                fm.popBackStack()
            }
        }
        transaction.replace(
            fragmentContainerId, activity.supportFragmentManager.findFragmentByTag(tag)
                ?: fragment, tag
        )

        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }

        try {
            transaction.commit()
        } catch (t: Throwable) {
            t.printStackTrace()
        }

    }


    fun removeFragment(fragment: Fragment, activity: FragmentActivity) {
        activity.supportFragmentManager
            .beginTransaction()
            .remove(fragment)
            .commit()
    }

    fun fragmentContainerIsEmpty(activity: FragmentActivity): Boolean {
        return activity.supportFragmentManager.findFragmentById(fragmentContainerId) == null
    }

}