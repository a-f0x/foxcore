package ru.f0xdev.appcoreexample.presentation.main.root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main_root.*
import ru.f0xdev.appcoreexample.R
import ru.f0xdev.appcoreexample.presentation.main.chats.ChatsListFragment
import ru.f0xdev.appcoreexample.presentation.main.settings.SettingsFragment
import ru.f0xdev.appcoreexample.presentation.main.users.UsersListFragment
import ru.f0xdev.f0xcore.base.ABaseFragment

class RootMainFragment : ABaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_root, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewpager.offscreenPageLimit = 3

        viewpager.adapter =
            MainPagerAdapter(listOf(UsersListFragment(), ChatsListFragment(), SettingsFragment()), childFragmentManager)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_user -> {
                    viewpager.currentItem = 0
                    true
                }
                R.id.nav_chats -> {
                    viewpager.currentItem = 1
                    true
                }
                R.id.nav_settings -> {
                    viewpager.currentItem = 2
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

}