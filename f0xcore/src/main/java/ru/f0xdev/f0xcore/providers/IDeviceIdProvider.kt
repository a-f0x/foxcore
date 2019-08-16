package ru.f0xdev.f0xcore.providers

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

interface IDeviceIdProvider {
    val deviceId: String
}


class DefaultDeviceIdProvider(private val context: Context) : IDeviceIdProvider {
    @SuppressLint("HardwareIds")
    override val deviceId: String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}