package ru.f0xdev.f0xcore.auth

class AuthEventListenerStub : IAuthEventListener {
    var onAccessUpdateCalled = false
    var onLoginCalled = false
    var onLogoutCalled = false
    override fun onAccessTokenUpdated() {
        onAccessUpdateCalled = true
    }

    override fun onLogin() {
        onLoginCalled = true
    }

    override fun onLogout() {
        onLogoutCalled = true
    }
}