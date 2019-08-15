package ru.f0xdev.f0xcore.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import java.util.*

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}


fun Activity.openKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT)
}

fun ViewGroup.asSequence(): Sequence<View> = (0 until childCount).asSequence().map({ getChildAt(it) })

val ViewGroup.views: List<View> get() = asSequence().toList()

val ViewGroup.viewsRecursive: List<View>
    get() = views.flatMap {
        when (it) {
            is ViewGroup -> listOf(it).plus(it.viewsRecursive)
            else -> listOf(it)
        }
    }

fun View.relativeTopTo(view: View): Int {
    if (parent == null)
        return top

    if (parent == view)
        return top

    return top + (parent as View).relativeTopTo(view)
}

fun TextView.onChange(onAfter: (String) -> Unit) {

    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            onAfter(s.toString())
        }
    })
}


inline fun <reified T> FragmentActivity.getTypedFragmentByTag(tag: String): T? {
    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: return null
    if (fragment is T)
        return fragment
    return null
}

fun String.underscoreToCamel(): String {
    if (!contains('_'))
        return this
    val wordsIterator = this.splitToSequence('_').iterator()
    val builder = StringBuilder()
    var isFirst = true
    wordsIterator.forEach {
        if (it.isEmpty())
            return@forEach
        if (isFirst) {
            builder.append(it.toLowerCase())
            isFirst = false
        } else {
            builder.append(it.toLowerCase().capitalize())
        }
    }
    return builder.toString()
}

fun Context.getAppUpdateTime(): Date {
    return Date(
        packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_INSTRUMENTATION
        )
            .lastUpdateTime
    )
}

