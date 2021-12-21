package com.base.baselibrary.viewmodel

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.base.baselibrary.utils.exhaustive
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class EventViewModel<EventClass : Any> : ViewModel() {

    protected val screenEventChannel = Channel<EventClass>()
    val screenEvent = screenEventChannel.receiveAsFlow()
    val defaultCoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->

        }

    fun receiveFlow(fragment: Fragment, onCollect: (EventClass) -> Unit) {
        fragment.lifecycleScope.launchWhenStarted {
            screenEvent.collect {
                onCollect.invoke(it)
            }
        }
    }

    fun receiveFlow(activity: AppCompatActivity, onCollect: (EventClass) -> Unit) {
        activity.lifecycleScope.launchWhenStarted {
            screenEvent.collect {
                onCollect.invoke(it)
            }
        }
    }

    fun sendIO(eventClass: EventClass, doActionBeforeSend: () -> Unit = {}) =
        viewModelScope.launch(Dispatchers.IO) {
            doActionBeforeSend.invoke()
            screenEventChannel.send(eventClass)
        }

    fun send(eventClass: EventClass, doActionBeforeSend: () -> Unit = {}) = viewModelScope.launch {
        doActionBeforeSend.invoke()
        screenEventChannel.send(eventClass)
    }

    fun sendMain(eventClass: EventClass, doActionBeforeSend: () -> Unit = {}) =
        viewModelScope.launch(Dispatchers.Main) {
            doActionBeforeSend.invoke()
            screenEventChannel.send(eventClass)
        }

    suspend fun sendSuspendEvent(eventClass: EventClass) = screenEventChannel.send(eventClass)
}