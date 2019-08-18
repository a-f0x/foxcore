package ru.f0xdev.f0xcore.auth.net

import okhttp3.Call
import okhttp3.Connection
import okhttp3.Interceptor
import ru.f0xdev.f0xcore.auth.IAuthEventListener
import ru.f0xdev.f0xcore.auth.IAuthManager
import ru.f0xdev.f0xcore.auth.models.AccessToken
import java.util.*
import java.util.concurrent.TimeUnit


abstract class AuthManagerTestStub : IAuthManager {
    private val listeners = Collections.synchronizedCollection(ArrayList<IAuthEventListener>())
    override fun addListener(listener: IAuthEventListener) {
        if (!listeners.contains(listener))
            listeners.add(listener)
    }

    override fun removeListener(listener: IAuthEventListener) {
        listeners.remove(listener)
    }

    override fun getToken(): String = "access_token"
    override fun getApiKey(): String = "api_key"
    override fun getAuthType(): String? = "Bearer"


    override fun logout() {}

    override fun setAccessToken(accessToken: AccessToken) {}
    override fun login(username: String, password: String) {}
}

abstract class BaseInterceptorChain : Interceptor.Chain {
    override fun writeTimeoutMillis(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun call(): Call {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun withWriteTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun connectTimeoutMillis(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun connection(): Connection? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun withConnectTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun withReadTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun readTimeoutMillis(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}