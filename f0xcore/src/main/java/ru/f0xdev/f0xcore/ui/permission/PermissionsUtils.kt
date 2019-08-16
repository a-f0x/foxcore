package ru.f0xdev.f0xcore.ui.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

const val REQUEST_WRITE_EXTERNAL_STORAGE = 99
const val REQUEST_PERMISSION_CAMERA_AND_STORAGE = 100
const val REQUEST_CODE_RECEIVE_SMS_PERMISSION = 101
const val REQUEST_CODE_READ_PHONE_STATE_PERMISSION_AND_CALL_LOG = 102
const val REQUEST_ACCESS_FINE_LOCATION = 103
const val REQUEST_READ_AND_WRITE_CALENDAR = 104


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


