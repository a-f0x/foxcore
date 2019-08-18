package ru.f0xdev.f0xcore.auth

interface IAuthEventListener {
    fun onAccessTokenUpdated()
    fun onLogin()
    fun onLogout()
}