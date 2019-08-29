package ru.f0xdev.f0xcore.ui.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


fun Activity.requestPermissionsSafely(
    permissions: List<String>,
    requestCode: Int
) {
    if (Build.VERSION.SDK_INT < 23) return
    ActivityCompat.requestPermissions(this, permissions.toTypedArray(), requestCode)
}

fun Fragment.requestPermissionsSafely(
    permissions: List<String>,
    requestCode: Int
) {
    if (Build.VERSION.SDK_INT < 23) return
    this.requestPermissions(permissions.toTypedArray(), requestCode)
}


fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED


