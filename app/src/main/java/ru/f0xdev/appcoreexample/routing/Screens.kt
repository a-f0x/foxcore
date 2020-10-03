package ru.f0xdev.appcoreexample.routing

import androidx.fragment.app.Fragment
import ru.f0xdev.appcoreexample.auth.AuthFragment
import ru.f0xdev.appcoreexample.main.root.RootMainFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen


object Screens {

    object AuthScreen : SupportAppScreen() {
        override fun getFragment(): Fragment? = AuthFragment()
    }

    object MainScreen : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return RootMainFragment()
        }
    }
}

