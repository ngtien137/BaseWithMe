package com.base.baselibrary.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.base.baselibrary.viewmodel.Event
import com.base.baselibrary.viewmodel.EventType
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

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

fun <E> MutableLiveData<E>.setNull() {
    value = null
}

fun <E> MutableLiveData<E>.postNull() {
    postValue(null)
}

fun <E> MutableLiveData<E>.postIfChanged(newValue: E) {
    if (this.value != newValue)
        postValue(this.value)
}

fun <E> MutableLiveData<E>.setIfChanged(newValue: E) {
    if (this.value != newValue)
        value = this.value
}

fun MutableLiveData<Boolean>.postReverseBoolean(defaultValue: Boolean = false) {
    val currentValue = value ?: defaultValue
    postValue(!currentValue)
}

fun MutableLiveData<Boolean>.setTrue() {
    value = true
}

fun MutableLiveData<Boolean>.setFalse() {
    value = false
}

fun MutableLiveData<Boolean>.postTrue() {
    postValue(true)
}

fun MutableLiveData<Boolean>.postFalse() {
    postValue(false)
}

fun MutableLiveData<Boolean>.setReverseBoolean(defaultValue: Boolean = false): Boolean {
    val currentValue = value ?: defaultValue
    val reverseValue = !currentValue
    value = reverseValue
    return reverseValue
}

fun <T> List<T>?.toArrayList() = ArrayList<T>().also { it.addAll(this ?: listOf()) }

inline fun <reified T> T.toArrayList() = ArrayList<T>().also { list -> list.add(this) }
inline fun <reified T> T.toStack() = Stack<T>().also { list -> list.add(this) }

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

inline fun <reified T> mutableLiveData(defaultValue: T?) =
    ObjectLazy<T, MutableLiveData<T>>(defaultValue)

inline fun <reified T> liveData(defaultValue: T?) =
    ObjectLazy(defaultValue)

class ObjectLazy<T, LD : LiveData<T>>(private val defaultValue: T?) : Lazy<LD> {
    private var cached: LD? = null

    override val value: LD
        get() {
            var data = cached
            if (data == null) {
                data = MutableLiveData(defaultValue) as LD
            }
            return data
        }

    override fun isInitialized() = cached != null
}

//region list

public fun <T> stackOf(): Stack<T> = Stack()

public fun <T> stackOf(vararg items: T): Stack<T> = Stack<T>().also { it.addAll(items) }

fun <T> LiveData<Stack<T>>.hasItem(item: T): Boolean = this.value?.let {
    it.search(item) != -1
} ?: false

fun <T> LiveData<Stack<T>>.getValueOrEmpty() = this.value ?: stackOf()
fun <T> LiveData<List<T>>.getValueOrEmpty() = this.value ?: listOf()
fun <T> LiveData<ArrayList<T>>.getValueOrEmpty() = this.value ?: arrayListOf()

//endregion