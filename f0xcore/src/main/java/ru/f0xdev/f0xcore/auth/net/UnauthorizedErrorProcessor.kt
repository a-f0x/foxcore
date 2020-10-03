package ru.f0xdev.f0xcore.auth.net

import finance.robo.android.accountservice.models.AuthException
import okhttp3.OkHttpClient
import ru.f0xdev.f0xcore.auth.IAuthManager
import ru.f0xdev.f0xcore.net.IResponseErrorProcessor
import java.io.IOException
import java.util.concurrent.locks.ReentrantLock

class UnauthorizedErrorProcessor(
    private val authManager: IAuthManager

) : IResponseErrorProcessor {
    companion object {
        const val HTTP_CODE = 401
    }

    private val lock = ReentrantLock()
    var okHttpClient: OkHttpClient? = null

    override fun processRequestWithError(
        errorBody: String?,
        errorCode: Int
    ): Boolean {
        if (lock.tryLock()) {
            try {
                authManager.invalidateToken()
                authManager.getToken()
            } catch (aue: AuthException) {
                processAuthException(aue)
                return false
            } finally {
                lock.unlock()
            }
        }
        lock.lock()
        lock.unlock()
        return true
    }

    private fun cancelAllRequests() {
        okHttpClient?.dispatcher?.cancelAll()
    }

    private fun processAuthException(aue: AuthException) {
        when (aue.type) {
            AuthException.Type.REFRESH_TOKEN_EXPIRED -> {
                cancelAllRequests()
            }
            AuthException.Type.NETWORK -> {
                throw IOException(aue.message)
            }
            else -> {
                throw IllegalStateException("Invalid AuthException type $aue")
            }
        }
    }
}
