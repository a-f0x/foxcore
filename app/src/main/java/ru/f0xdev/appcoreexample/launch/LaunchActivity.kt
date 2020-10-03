package ru.f0xdev.appcoreexample.launch

import android.os.Bundle
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.inject
import ru.f0xdev.appcoreexample.R
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.base.IFragmentActivity
import ru.f0xdev.f0xcore.base.navigation.ABaseNavigationActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator


class LaunchActivity : ABaseNavigationActivity(), IFragmentActivity, BaseView {

    private val p: LaunchPresenter by inject()

    @InjectPresenter
    lateinit var presenter: LaunchPresenter

    @ProvidePresenter
    fun provide(): LaunchPresenter = p

    override val fragmentContainerId: Int = R.id.fragmentContainer

    override val navigatorHolder: NavigatorHolder by inject()

    override val navigator: Navigator = SupportAppNavigator(
        this, fragmentContainerId
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }
}