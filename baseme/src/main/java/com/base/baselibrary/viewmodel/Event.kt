package com.base.baselibrary.viewmodel

import androidx.lifecycle.MutableLiveData

class Event(
    var isLoading: Boolean = false,
    var typeEvent: Int = EventType.LOADING,
    var message: String = ""
) {
}

object EventType {
    const val NONE = -1
    const val LOADING = 0
}

fun MutableLiveData<Event>.postLoading(loading: Boolean = true) {
    postValue(Event(loading))
}

fun MutableLiveData<Event>.setLoading(loading: Boolean = true) {
    value = Event(loading)
}

fun MutableLiveData<Event>.isLoading() = value?.isLoading == true
fun MutableLiveData<Event>.isNotLoading() = value?.isLoading != true