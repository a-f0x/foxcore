package ru.f0xdev.appcoreexample.presentation.auth

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

interface AuthView : BaseView {

}

class AuthFragment : ABaseFragment(), AuthView, ValidatableView {

    private val p: AuthPresenter by inject()

    @InjectPresenter
    lateinit var presenter: AuthPresenter

    @ProvidePresenter
    fun provide(): AuthPresenter = p

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun progressLayout(): View? = progressView

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


    override fun showValidationError(details: Map<String, List<String>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}