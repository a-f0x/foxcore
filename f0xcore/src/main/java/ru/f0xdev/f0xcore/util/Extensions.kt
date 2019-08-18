package ru.f0xdev.f0xcore.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.views.ValidatableInput
import java.lang.reflect.InvocationTargetException

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

fun View.extractString(validationError: InputValidationError): String {
    return if (validationError.message.isNotEmpty()) {
        validationError.message
    } else {
        context.getString(validationError.messageId, *validationError.args)
    }
}

fun ViewGroup.getValidatableViews(): List<ValidatableInput> {
    return viewsRecursive
        .filter { it is ValidatableInput && it.validationRules.isNotEmpty() } as List<ValidatableInput>
}

fun Fragment.getValidatableViews(): List<ValidatableInput> {
    view?.let {
        if (it is ViewGroup)
            return it.getValidatableViews()
        return emptyList()
    }
    return emptyList()
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

inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
    when (T::class) {
        Boolean::class -> return this.getBoolean(key, defaultValue as Boolean) as T
        Float::class -> return this.getFloat(key, defaultValue as Float) as T
        Int::class -> return this.getInt(key, defaultValue as Int) as T
        Long::class -> return this.getLong(key, defaultValue as Long) as T
        String::class -> return this.getString(key, defaultValue as String) as T
        else -> {
            if (defaultValue is Set<*>) {
                return this.getStringSet(key, defaultValue as Set<String>) as T
            }
        }
    }
    return defaultValue
}

inline fun <reified T> SharedPreferences.put(key: String, value: T): T {
    val editor = this.edit()

    when (T::class) {
        Boolean::class -> editor.putBoolean(key, value as Boolean)
        Float::class -> editor.putFloat(key, value as Float)
        Int::class -> editor.putInt(key, value as Int)
        Long::class -> editor.putLong(key, value as Long)
        String::class -> editor.putString(key, value as String)
        else -> {
            if (value is Set<*>) {
                editor.putStringSet(key, value as Set<String>)
            }
        }
    }

    editor.commit()
    return value
}

/**
 * Эта функция выполняет блок пока не истечет количество попыток
 * по истечению количества попыток будет возвращено дефолтовое значение
 *
 * @param times             - количество попыток выполнения блока
 * @param initialDelay      - стартовое значение задержки перед следующим выполнением блока
 * @param maxDelay          - максимальное время задержки между вызовами блока
 * @param factor            - фактор увеличения задержки
 * @param block             - суспенд функция которая будет выполнятся
 * @param defaultValue      - дефолтовое значение которое надо вернуть если за заданное количестов попыток  функция не отработала корректно
 * @param retryExceptionClass    - тот эксепшен который нужно игнорировать и повторять отложенный вызов функции.
 *                              Если функция выбросит какой либо другой ексепшен то он будет проброшен наверх
 *
 * P.S. Использовать эту функцию и генерировать эксепшен самому что бы что то повторить не очень варик на самом деле, потому что
 * генерация эксепшенов и их отлов довольно затратны на рантайме, но рефакторить будем когда это реально принесет проблемы.
 *
 * */
suspend fun <T, E : Throwable> retryException(
    times: Int = Int.MAX_VALUE,
    initialDelay: Long = 200,
    maxDelay: Long = 1500,
    factor: Double = 2.0,
    block: suspend () -> T?,
    defaultValue: T? = null,
    retryExceptionClass: Class<E>
): T? {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: Exception) {
            if (e.javaClass != retryExceptionClass) {
                throw e
            }
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return defaultValue
}


inline fun <reified T> Gson.fromJsonTyped(json: String): T {
    return this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}


fun Any?.getTagForLogging(): String {
    this ?: return "Null object"
    val name = this.javaClass.simpleName
    if (name.length > 23)
        return name.substring(0..21)
    return name

}

fun Any?.logInfo(info: String) {
    Log.i(getTagForLogging(), info)
}

fun Any?.logDebug(debug: String, throwable: Throwable?) {
    Log.d(getTagForLogging(), debug, throwable)
}

fun Any?.logWarn(warn: String) {
    Log.w(getTagForLogging(), warn)
}

fun Any.logError(error: String, throwable: Throwable?) {
    Log.e(getTagForLogging(), error, throwable)
}


fun Any.isLateinitPropertyInitialized(lateinitFieldName: String): Boolean {
    val prop = javaClass.kotlin.members.firstOrNull { it.name == lateinitFieldName }
    checkNotNull(prop) { "Field not found!" }
    return try {
        prop.call(this)
        true
    } catch (ex: InvocationTargetException) {
        if (ex.targetException is UninitializedPropertyAccessException)
            return false
        else
            throw ex
    }
}

