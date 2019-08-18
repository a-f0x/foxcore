package ru.f0xdev.f0xcore.auth

import android.accounts.AccountManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import ru.f0xdev.f0xcore.auth.di.AuthServiceDependencies
import ru.f0xdev.f0xcore.deps.findComponentDependencies

class AuthenticatorService : Service() {

    private lateinit var authenticator: AccountAuthenticator

    override fun onCreate() {
        super.onCreate()
        val deps = findComponentDependencies<AuthServiceDependencies>()
        authenticator = AccountAuthenticator(
            this,
            AccountManager.get(this),
            deps.provideAuthRemoteDataSource(),
            deps.provideITokenCryptographer()
        )
    }

    override fun onBind(intent: Intent?): IBinder {
        return authenticator.iBinder
    }
}