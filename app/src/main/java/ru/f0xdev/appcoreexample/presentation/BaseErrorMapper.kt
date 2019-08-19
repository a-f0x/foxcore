package ru.f0xdev.appcoreexample.presentation

import com.google.gson.Gson
import retrofit2.HttpException
import ru.f0xdev.f0xcore.presentation.errors.ErrorConsts
import ru.f0xdev.f0xcore.presentation.errors.IError
import ru.f0xdev.f0xcore.presentation.errors.IErrorMapper
import ru.f0xdev.f0xcore.util.fromJsonTyped
import java.io.IOException

open class BaseErrorMapper(private val gson: Gson) : IErrorMapper {
    open val knownErrorMessages = mapOf<String, Map<String, String>>()

    override fun mapThrowableToError(throwable: Throwable): IError {
        return when (throwable) {
            is HttpException -> {
                mapHttpExceptionToError(throwable)
            }
            is IOException -> {
                createNetworkError(throwable)
            }
            else -> createUnknownError(throwable)
        }
    }

    protected open fun mapHttpExceptionToError(exception: HttpException): IError {
        val errorString =
            exception.response()?.errorBody()?.string() ?: throw IllegalStateException("Empty error body!!")
        return try {
            val details = gson.fromJsonTyped<Map<String, List<String>>>(errorString)
            if (details.isNotEmpty())
                Error(ErrorConsts.VALIDATION_ERROR, mapErrorDetails(details), exception)
            else
                Error(ErrorConsts.UNKNOWN_ERROR, mapOf(), exception)

        } catch (t: Throwable) {
            t.printStackTrace()
            Error(ErrorConsts.UNKNOWN_ERROR, mapOf(), exception)
        }
    }

    protected open fun createNetworkError(throwable: Throwable): IError =
        Error(
            ErrorConsts.NETWORK_ERROR,
            emptyMap(),
            throwable
        )

    protected open fun createUnknownError(throwable: Throwable): IError =
        Error(
            ErrorConsts.UNKNOWN_ERROR,
            emptyMap(),
            throwable
        )


    open fun mapErrorDetails(details: Map<String, List<String>>): Map<String, List<String>> {
        val map = mutableMapOf<String, List<String>>()
        details.forEach { entry ->
            map[entry.key] = entry.value.map { getValidationErrorMessage(entry.key, it.split(".")[1]) }
        }
        return map
    }

    open fun getValidationErrorMessage(fieldTag: String, validationErrorType: String): String {
        val errors = knownErrorMessages[fieldTag]
        val errorText = errors?.get(validationErrorType)
        return errorText ?: "Неизвестная ошибка"
    }

}