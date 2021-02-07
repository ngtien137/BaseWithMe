package com.base.baselibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

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