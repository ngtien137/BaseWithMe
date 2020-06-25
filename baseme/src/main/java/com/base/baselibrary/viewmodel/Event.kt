package com.base.baselibrary.viewmodel

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