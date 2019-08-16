package ru.f0xdev.f0xcore.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.f0xdev.f0xcore.R

class SimpleYesNoDialog : DialogFragment() {
    var onActionClick: ((positiveAnswer: Boolean) -> Unit)? = null

//
//    var onClickListener: View.OnClickListener? = null
//    var onNegativeClickListener: View.OnClickListener? = null

    companion object {
        private const val TITLE_RES_ID = "SimpleYesNoDialog.TITLE_RES_ID"
        private const val TITLE_TEXT = "SimpleYesNoDialog.TITLE_TEXT"
        private const val MESSAGE_TEXT = "SimpleYesNoDialog.MESSAGE_TEXT"
        private const val MESSAGE_RES_ID = "SimpleYesNoDialog.MESSAGE_RES_ID"
        private const val POSITIVE_BUTTON_RES_ID = "SimpleYesNoDialog.POSITIVE_BUTTON_RES_ID"
        private const val NEGATIVE_RES_ID = "SimpleYesNoDialog.NEGATIVE_BUTTON_RES_ID"
        private const val IS_ONE_BUTTON = "SimpleYesNoDialog.IS_ONE_BUTTON"
        private const val ICON_RES_ID = "SimpleYesNoDialog.ICON_RES_ID"

        fun newInstance(
            message: String,
            title: String,
            @StringRes positiveButtonResId: Int = R.string.yes,
            @StringRes negativeButtonResId: Int = R.string.no,
            oneButton: Boolean = false,
            @DrawableRes iconResId: Int = 0
        ): SimpleYesNoDialog {
            val dialog = SimpleYesNoDialog()
            val args = Bundle()
            if (!TextUtils.isEmpty(message)) {
                args.putString(MESSAGE_TEXT, message)
            }
            if (!TextUtils.isEmpty(title)) {
                args.putString(TITLE_TEXT, title)
            }
            args.putInt(POSITIVE_BUTTON_RES_ID, positiveButtonResId)
            args.putInt(NEGATIVE_RES_ID, negativeButtonResId)
            args.putBoolean(IS_ONE_BUTTON, oneButton)
            args.putInt(ICON_RES_ID, iconResId)
            dialog.arguments = args
            return dialog
        }

        fun newInstance(
            @StringRes messageResId: Int,
            @StringRes titleResId: Int = 0,
            @StringRes positiveButtonResId: Int = R.string.yes,
            @StringRes negativeButtonResId: Int = R.string.no,
            oneButton: Boolean = false,
            @DrawableRes iconResId: Int = 0
        ): SimpleYesNoDialog {
            val dialog = newInstance("", "", positiveButtonResId, negativeButtonResId, oneButton, iconResId)
            dialog.arguments?.putInt(MESSAGE_RES_ID, messageResId)
            dialog.arguments?.putInt(TITLE_RES_ID, titleResId)
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let { arguments ->
            val iconResId = arguments.getInt(ICON_RES_ID)
            val messageResId = arguments.getInt(MESSAGE_RES_ID)
            val messageText = arguments.getString(MESSAGE_TEXT) ?: getString(messageResId)
            val titleResId = arguments.getInt(TITLE_RES_ID)
            val titleText = arguments.getString(TITLE_TEXT)
                ?: if (titleResId > 0) getString(titleResId) else ""
            val positiveButtonResId = arguments.getInt(POSITIVE_BUTTON_RES_ID)
            val negativeButtonResId = arguments.getInt(NEGATIVE_RES_ID)
            val isOneButton = arguments.getBoolean(IS_ONE_BUTTON)
            val builder = AlertDialog.Builder(activity!!)
            builder.setMessage(messageText)
                .setPositiveButton(positiveButtonResId) { dialog, _ ->
                    dialog.dismiss()
                    onClickListener?.onClick(view)
                }
            if (!isOneButton) {
                builder.setNegativeButton(negativeButtonResId) { dialog, _ ->
                    dialog.dismiss()
                    onNegativeClickListener?.onClick(view)
                }
            }

            if (!TextUtils.isEmpty(titleText)) {
                builder.setTitle(titleText)
            }
            if (iconResId > 0) {
                builder.setIcon(iconResId)
            }
            return builder.create()
        } ?: throw IllegalArgumentException("no arguments")
    }
}