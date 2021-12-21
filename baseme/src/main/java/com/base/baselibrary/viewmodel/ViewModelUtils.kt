package com.base.baselibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

object ViewModelUtils {

    fun <T> ViewModel.doJob(
        doIn: (scopeDoIn: CoroutineScope) -> T,
        doOut: (T) -> Unit = {},
        dispatcherIn: CoroutineDispatcher = Dispatchers.IO,
        dispatcherOut: CoroutineDispatcher = Dispatchers.Main
    ): Job {
        return viewModelScope.launch(dispatcherIn) {
            val data = doIn(this)
            withContext(dispatcherOut) {
                doOut(data)
            }
        }
    }

    fun <T> CoroutineScope.doJob(
        doIn: (scopeDoIn: CoroutineScope) -> T,
        doOut: (T) -> Unit = {},
        dispatcherIn: CoroutineDispatcher = Dispatchers.IO,
        dispatcherOut: CoroutineDispatcher = Dispatchers.Main
    ): Job {
        return launch(dispatcherIn) {
            val data = doIn(this)
            withContext(dispatcherOut) {
                doOut(data)
            }
        }
    }

    fun ViewModel.postDelay(doJob: () -> Unit, timeDelay: Long = 0) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(timeDelay)
            withContext(Dispatchers.Main) {
                doJob.invoke()
            }
        }
    }

    fun ViewModel.scopeMainThread(doJob: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            doJob.invoke()
        }
    }

    fun <T : Any> EventViewModel<T>.scopeMainThreadWithTryCatch(
        onError: (error: Throwable) -> Unit = {},
        doJob: () -> Unit
    ) {
        val exceptionHandle = CoroutineExceptionHandler { _, throwable ->
            onError.invoke(throwable)
        }
        viewModelScope.launch(Dispatchers.Main + exceptionHandle) {
            doJob.invoke()
        }
    }

    fun <T : Any> EventViewModel<T>.scopeIOWithTryCatch(
        onError: (error: Throwable) -> Unit = {},
        doJob: () -> Unit
    ) {
        val exceptionHandle = CoroutineExceptionHandler { _, throwable ->
            onError.invoke(throwable)
        }
        viewModelScope.launch(Dispatchers.Main + exceptionHandle) {
            doJob.invoke()
        }
    }
}