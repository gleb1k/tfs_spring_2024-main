package ru.glebik.core.presentation.mvi

import android.util.Log
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseExecutor<in Intent : Any, in Action : Any, in State : Any, Message : Any, Label : Any>(
    mainContext: CoroutineContext = Dispatchers.Main.immediate,
) : CoroutineExecutor<Intent, Action, State, Message, Label>(mainContext = mainContext) {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        onException(throwable)
    }

    protected fun CoroutineScope.launchSafe(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job = launch(context + handler, block = block)

    protected fun <T> CoroutineScope.asyncSafe(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> T
    ): Deferred<T>  = async(context + handler, block = block)

    protected open fun onException(exception: Throwable) {
        Log.e(TAG, "Exception handled", exception)
    }

    companion object {
        const val TAG = "ExecutorTag"
    }
}