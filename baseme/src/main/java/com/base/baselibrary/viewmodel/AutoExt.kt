package com.base.baselibrary.viewmodel

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.base.baselibrary.views.ext.loge
import java.lang.Exception
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

private val hashMapSingleton by lazy {
    HashMap<String, Any?>()
}

fun getAllListSingleton() = hashMapSingleton

@Synchronized
fun Class<*>.getSingleton(): Any {
    if (hashMapSingleton[this.simpleName] == null)
        hashMapSingleton[this.simpleName] = try {
            this.newInstance()
        } catch (e: InstantiationException) {
            getDefaultValue(this.name)
        }
    /**
     * If your class use this function not have empty constructor, it will cause error
     */
    return hashMapSingleton[this.simpleName]!!
}

fun <T> T.applySingleton(): T {
    hashMapSingleton[this!!::class.java.simpleName] = this
    return this
}

fun clearAllSingleton() {
    hashMapSingleton.clear()
}

fun Class<*>.clearSingleton() {
    hashMapSingleton.remove(this.simpleName)
}

fun getDefaultValue(typeName: String): Any? {
    return when (typeName) {
        Int::class.java.name
        -> 0
        String::class.java.name
        -> ""
        Float::class.java.name
        -> 0f
        Long::class.java.name
        -> 0L
        Boolean::class.java.name
        -> false
        else -> null
    }
}

fun Class<*>.createInstanceWithAuto(): Any? {
    val listConstructor = this.declaredConstructors
    val listParams = ArrayList<Any?>()
    var resultConstructor: Constructor<*>? = null
    var maxParamsCount = 0
    var isSingleton = true
    for (constructor in listConstructor) {
        val paramsCount = constructor.parameterTypes.size
        val declaredAnnotations = constructor.declaredAnnotations
        for (annotation in declaredAnnotations) {
            if (annotation is Auto && paramsCount >= maxParamsCount) {
                isSingleton = annotation.singleton
                resultConstructor = constructor
                maxParamsCount = paramsCount
            }
        }
    }
    if (resultConstructor != null) {
        val paramsTypes = resultConstructor.parameterTypes
        for (i in 0 until maxParamsCount) {
            val defValue = getDefaultValue(paramsTypes[i].name)
            val param = try {
                if (isSingleton) {
                    val checkIfPrimitiveDataType =
                        defValue != null //Kiểm tra xem có phải kiểu dữ liệu nguyên thủy hay ko
                    // , nếu là kiểu dữ liệu nguyên thủy thì ko tạo singleton
                    if (checkIfPrimitiveDataType)
                        defValue
                    else
                        paramsTypes[i].getSingleton()
                } else {
                    paramsTypes[i].createInstanceWithAuto()
                }
            } catch (e: Exception) {
                defValue
            }
            listParams.add(param)
        }
    }
    if (resultConstructor == null) {
        return try {
            this.newInstance()
        } catch (e: InvocationTargetException) {
            getDefaultValue(this.name)
        }
    }
    resultConstructor.isAccessible = true
    return resultConstructor.newInstance(*listParams.toArray())
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.autoViewModels(
) = createViewModelLazy(VM::class, { this.viewModelStore }, { AutoFactory() })

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.autoViewModels(
): Lazy<VM> {
    val factoryPromise = { AutoFactory() }
    return ViewModelLazy(VM::class, { viewModelStore }, factoryPromise)
}
