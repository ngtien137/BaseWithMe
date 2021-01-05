package com.base.baselibrary.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.base.baselibrary.viewmodel.Event
import com.base.baselibrary.viewmodel.EventType
import java.math.BigDecimal

fun MutableLiveData<Event>.post(
    loading: Boolean = true,
    eventType: Int = EventType.LOADING,
    message: String = ""
) {
    value = Event(loading, eventType, message)
}

fun MutableLiveData<Event>.post(eventType: Int = EventType.LOADING) {
    value = Event(true, eventType, "")
}


fun <E> LiveData<List<E>>.isEmptyList(): Boolean {
    return value.isNullOrEmpty()
}

fun <E> MutableLiveData<E>.postSelf() {
    postValue(this.value)
}

fun <E> MutableLiveData<E>.setSelf() {
    value = this.value
}

fun <E> MutableLiveData<E>.postIfChanged(newValue: E) {
    if (this.value != newValue)
        postValue(this.value)
}

fun <E> MutableLiveData<E>.setIfChanged(newValue: E) {
    if (this.value != newValue)
        value = this.value
}

fun MutableLiveData<Boolean>.postReverseBoolean() {
    val currentValue = value ?: false
    postValue(!currentValue)
}

fun MutableLiveData<Boolean>.setReverseBoolean() {
    val currentValue = value ?: false
    value = !currentValue
}


fun <T> Fragment.observer(liveData: LiveData<T>?, onChange: (T?) -> Unit) {
    liveData?.observe(viewLifecycleOwner, Observer(onChange))
}

fun <T> AppCompatActivity.observer(liveData: LiveData<T>?, onChange: (T?) -> Unit) {
    liveData?.observe(this, Observer(onChange))
}

fun Float.scale(numberDigitsAfterComma: Int): Float {
    return BigDecimal(this.toDouble()).setScale(numberDigitsAfterComma, BigDecimal.ROUND_HALF_EVEN)
        .toFloat()
}

fun Double.scale(numberDigitsAfterComma: Int): Double {
    return BigDecimal(this.toDouble()).setScale(numberDigitsAfterComma, BigDecimal.ROUND_HALF_EVEN)
        .toDouble()
}