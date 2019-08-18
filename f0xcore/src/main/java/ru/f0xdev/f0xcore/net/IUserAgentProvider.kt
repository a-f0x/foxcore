package ru.f0xdev.f0xcore.net

import android.os.Build
import java.util.*


data class UserAgent(val headerKey: String, val userAgentValue: String)

interface IUserAgentProvider {
    fun provideUserAgent(): UserAgent
}

class UserAgentProvider(appPackageName: String, appVersion: String) : IUserAgentProvider {
    private val userAgent = UserAgent(
        "User-Agent",
        String.format(
            Locale.US,
            "%s/%s (Android %s; %s; %s %s; %s)",
            appPackageName,
            appVersion,
            Build.VERSION.RELEASE,
            Build.MODEL,
            Build.BRAND,
            Build.DEVICE,
            Locale.getDefault().language
        )
    )

    override fun provideUserAgent(): UserAgent = userAgent
}