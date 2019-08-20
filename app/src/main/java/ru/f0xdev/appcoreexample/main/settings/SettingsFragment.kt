package ru.f0xdev.appcoreexample.main.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.header_settings_fragment.view.*
import org.koin.android.ext.android.inject
import ru.f0xdev.appcoreexample.R
import ru.f0xdev.appcoreexample.main.users.UsersListItem
import ru.f0xdev.f0xcore.base.ABaseFragment
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.ui.dialogs.SimpleYesNoDialog

data class UserProfile(
    val user: UsersListItem
)


interface SettingsView : BaseView {
    fun setUserProfile(userProfile: UserProfile)
    fun showConfirmationDialog()
}


class SettingsFragment : ABaseFragment(), SettingsView, NavigationView.OnNavigationItemSelectedListener {

    private val p: SettingsFragmentPresenter by inject()

    @InjectPresenter
    lateinit var presenter: SettingsFragmentPresenter

    @ProvidePresenter
    fun provide() = p

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        setHasOptionsMenu(true)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_setting_about -> {
                presenter.onAboutClick()
                true
            }
            R.id.nav_settings_logout -> {
                presenter.onLogoutClick()
                true
            }
            else -> {
                false
            }

        }
    }


    @SuppressLint("SetTextI18n")
    override fun setUserProfile(userProfile: UserProfile) {
        navView.getHeaderView(0).profileName.text =
            "${userProfile.user.lastName} ${userProfile.user.firstName}"
    }

    override fun showConfirmationDialog() {
        val d = SimpleYesNoDialog.newInstance(
            R.string.logout_confirmation_message,
            R.string.nav_logout_title
        )
        d.onActionClick = {
            if (it)
                presenter.logout()
        }
        d.show(fragmentManager, "")
    }

}