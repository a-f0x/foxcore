package ru.f0xdev.appcoreexample.launch

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.koin.android.ext.android.inject
import ru.f0xdev.appcoreexample.R
import ru.f0xdev.appcoreexample.auth.AuthFragment
import ru.f0xdev.appcoreexample.main.root.RootMainFragment
import ru.f0xdev.f0xcore.base.ABaseActivity
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.base.IFragmentActivity

interface MainView : BaseView {
    fun showAuthFragment()
    fun showMainFragment()
}

class MainActivity : ABaseActivity(),
    IFragmentActivity,

    MainView {

    private val p: LaunchPresenter by inject()
    @InjectPresenter
    lateinit var presenter: LaunchPresenter

    @ProvidePresenter
    fun provide(): LaunchPresenter = p

    override val fragmentContainerId: Int = R.id.fragmentContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }

    override fun showAuthFragment() {

        switchFragmentWithDefaultAmination(
            fragment = AuthFragment(),
            tag = AuthFragment::class.java.simpleName,
            activity = this,
            clearTopFragments = true
        )
    }

    override fun showMainFragment() {
        switchFragmentWithDefaultAmination(
            fragment = RootMainFragment(),
            tag = RootMainFragment::class.java.simpleName,
            activity = this,
            clearTopFragments = true
        )
    }

}