package ru.f0xdev.f0xcore.ui.permission

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import ru.f0xdev.f0xcore.R


class PermissionInstructionDialog : DialogFragment() {

    private val permissions = mutableListOf<String>()

    companion object {
        private const val KEY_ARG_MESSAGE_RES_ID = "PermissionInfoDialog.ARG_MESSAGE_RES_ID"
        private const val KEY_ARG_PERMISSIONS = "PermissionInfoDialog.ARG_PERMISSIONS"
        private const val KEY_ARG_REQUEST_CODE = "PermissionInfoDialog.ARG_REQUEST_CODE"

        fun newInstance(
            @StringRes messageResId: Int, permissions: ArrayList<String>,
            requestCode: Int
        ): PermissionInstructionDialog {
            val dialog = PermissionInstructionDialog()
            val args = Bundle()
            args.putInt(KEY_ARG_MESSAGE_RES_ID, messageResId)
            args.putStringArrayList(KEY_ARG_PERMISSIONS, permissions)
            args.putInt(KEY_ARG_REQUEST_CODE, requestCode)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val messageResId = arguments!!.getInt(KEY_ARG_MESSAGE_RES_ID)
        val requestCode = arguments!!.getInt(KEY_ARG_REQUEST_CODE)
        val builder = AlertDialog.Builder(activity!!)
        builder.setMessage(getString(messageResId))
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()

                val tFragment = targetFragment
                if (tFragment != null) {
                    tFragment.requestPermissionsSafely(permissions = permissions, requestCode = requestCode)
                } else {
                    this.activity?.requestPermissionsSafely(permissions = permissions, requestCode = requestCode)
                }
            }
        return builder.create()
    }

    /**
     * Отображает диалог если хотя бы один пермишн из списка не предоставлен
     * @param fragmentManager
     * @param context
     * @param tag
     * @return true если диалог был показан, false в противном случае
     */
    fun showIfRequired(fragmentManager: FragmentManager, context: Context, tag: String = ""): Boolean {
        permissions.clear()
        arguments!!.getStringArrayList(KEY_ARG_PERMISSIONS)?.let { permissions.addAll(it) }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false
        val requiredPermissions = permissions.filter {
            !context.hasPermission(it)
        }
        val showDialog = !requiredPermissions.isEmpty()
        if (showDialog) {
            show(fragmentManager, tag)
        }
        return showDialog
    }
}