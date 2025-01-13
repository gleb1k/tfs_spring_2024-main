package ru.glebik.core.utils

import java.io.IOException
import java.net.UnknownHostException


sealed interface ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>
    open class Failed(
        val exception: Throwable? = null,
        val errorMessage: String? = null,
        val code: Int? = null,
    ) : ResultWrapper<Nothing>

    class NetworkError(exception: Throwable) : Failed(exception, exception.message)
}

fun wrapError(throwable: Throwable): ResultWrapper<Nothing> {
    return when (throwable) {
        is UnknownHostException -> ResultWrapper.NetworkError(Throwable(message = "Connection problems"))
        //HttpException Retrofit2
        is RuntimeException -> ResultWrapper.NetworkError(Throwable(message = throwable.message))
        is IOException -> ResultWrapper.NetworkError(Throwable(message = "Server Error"))
        else -> ResultWrapper.Failed(throwable, throwable.message, null)
    }
}

fun parseErrorMessage(resultWrapper: ResultWrapper.NetworkError): String =
    resultWrapper.errorMessage ?: resultWrapper.exception?.message ?: BASE_MESSAGE_ERROR

private const val BASE_MESSAGE_ERROR = "Unknown error"