package ru.f0xdev.appcoreexample.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_auth.*
import org.koin.android.ext.android.inject
import ru.f0xdev.appcoreexample.R
import ru.f0xdev.f0xcore.base.ABaseFragment
import ru.f0xdev.f0xcore.base.BaseView
import ru.f0xdev.f0xcore.ui.inputvalidation.views.ValidatableView
import ru.f0xdev.f0xcore.util.getValidatableViews

interface AuthView : BaseView, ValidatableView {

}

class AuthFragment : ABaseFragment(), AuthView {

    private val p: AuthPresenter by inject()

    @InjectPresenter
    lateinit var presenter: AuthPresenter

    @ProvidePresenter
    fun provide(): AuthPresenter = p

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override val progressLayout: View? = progressView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterButton.setOnClickListener {
            validateInputs(
                getValidatableViews(),
                onSuccess = {
                    presenter.login(etEmail.text.toString().trim(), etPassword.text.toString().trim())
                }
            )
        }
    }


}