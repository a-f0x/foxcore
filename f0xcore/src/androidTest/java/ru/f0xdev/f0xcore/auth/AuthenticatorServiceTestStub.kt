package ru.f0xdev.f0xcore.auth

import android.accounts.AccountManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import ru.f0xdev.f0xcore.auth.net.IAuthRemoteDataSource

class AuthenticatorServiceTestStub : Service() {

    companion object {
        var cryptographer: ITokenCryptographer? = null
        var remoteDataSource: IAuthRemoteDataSource? = null
    }

    private lateinit var authenticator: AccountAuthenticator

    override fun onCreate() {
        super.onCreate()
        val cr = cryptographer
        val remote = remoteDataSource
        if (cr != null && remote != null) {

            authenticator = AccountAuthenticator(
                this,
                AccountManager.get(this),
                remote,
                cr
            )
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return authenticator.iBinder
    }
}