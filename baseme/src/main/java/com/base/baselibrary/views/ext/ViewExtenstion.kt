package com.base.baselibrary.views.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.RectF
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.base.baselibrary.utils.getApplication
import kotlinx.coroutines.*
import kotlin.math.roundToInt


/**
 * create a view model in activity
 */

var APPLOG = "APP_LOG"
private val logEnable = true
fun Any.loge(message: Any) {
    e(message)
}

fun Fragment.loge(message: Any) {
    e(message)
}

fun String.loge(message: Any) {
    if (logEnable) {
        Log.e(this, message.toString())
    }
}

private fun Any.e(message: Any) {
    if (logEnable) {
        Log.e(APPLOG + this::class.java.simpleName, message.toString())
    }
}

fun Activity.loge(message: Any) {
    Log.e(APPLOG + this::class.java.simpleName, message.toString())
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun appToast(message: Int) {
    getApplication().toast(message)
}

fun appToast(message: String) {
    getApplication().toast(message)
}

//region editText

fun Activity.hideKeyboard() {
    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.hideKeyboard(v: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(v.windowToken, 0)
}

fun Activity.showKeyBoard(v: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
}

fun View.isVisible() = this.visibility == View.VISIBLE
fun View.isInVisible() = this.visibility == View.INVISIBLE
fun View.isGone() = this.visibility == View.GONE

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.postScrollY(newScrollY: Number) {
    postScroll(scrollX, newScrollY)
}

fun View.postScrollX(newScrollX: Number) {
    postScroll(newScrollX, scrollY)
}

fun View.postScroll(newScrollX: Number, newScrollY: Number) {
    post {
        scrollTo(newScrollX.toInt(), newScrollY.toInt())
    }
}

fun EditText.showKeyboard() {
    this.requestFocus()
    if (context != null) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun EditText.hideKeyboard() {
    this.clearFocus()
    if (context != null) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }
}

//endregion

fun Activity.isPortrait(): Boolean {
    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        return true
    return false
}

fun View?.startAnimationResourceId(@AnimRes animResourceId: Int) {
    this?.startAnimation(AnimationUtils.loadAnimation(context, animResourceId))
}

fun View?.setAsIndicator(viewPager: ViewPager?) {
    this?.let { indicator ->
        indicator.post {
            viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    val translationOffset = (positionOffset + position) * indicator.width
                    val params = indicator.layoutParams as FrameLayout.LayoutParams
                    params.leftMargin = translationOffset.roundToInt()
                    indicator.layoutParams = params
                }

                override fun onPageSelected(position: Int) {

                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            })
        }
    }
}

/**
 * do a lambda function in main thread
 */
private var handlerDoingJob: Handler? = null
fun doInMainThread(doing: () -> Unit, delay: Long = 0) {
//    if (handlerDoingJob != null) {
//        clearDoInMain()
//    }
    handlerDoingJob = Handler(Looper.getMainLooper())
    if (delay <= 0L)
        handlerDoingJob!!.post {
            doing()
            clearDoInMain()
        }
    else
        handlerDoingJob?.postDelayed({
            doing()
            clearDoInMain()
        }, delay)
}

fun doInMainThread(doing: () -> Unit) {
    handlerDoingJob = Handler(Looper.getMainLooper())
    handlerDoingJob!!.post {
        doing()
        clearDoInMain()
    }
}

fun clearDoInMain() {
    handlerDoingJob?.removeCallbacksAndMessages(null)
}


fun AppCompatActivity.addScreenOnId(
    idContainer: Int,
    fragment: Fragment, toBackStack: Boolean = true,
    animIn: Int = android.R.anim.fade_in,
    animOut: Int = android.R.anim.fade_out
) {
    supportFragmentManager.beginTransaction().apply {
        setCustomAnimations(animIn, animOut, animIn, animOut)
        add(idContainer, fragment)
        if (toBackStack)
            addToBackStack(fragment::class.java.simpleName)
        commitAllowingStateLoss()
    }
}

fun AppCompatActivity.replaceScreenOnId(
    idContainer: Int,
    fragment: Fragment, toBackStack: Boolean = true,
    animIn: Int = android.R.anim.fade_in,
    animOut: Int = android.R.anim.fade_out
) {
    supportFragmentManager.beginTransaction().apply {
        replace(idContainer, fragment)
        if (toBackStack)
            addToBackStack(fragment::class.java.simpleName)
        commitAllowingStateLoss()
    }
}

/**
 * Thực hiện 1 câu lệnh và trả về kết quả khi hoàn thành
 * Có thể xen giữa câu lệnh khác khi đang chạy (Như kiểu lấy progress loading hiện tại) bằng cách
 * sử dụng hàm doJobInThreadType trong doIn
 */
fun <T> async(
    doIn: (scopeDoIn: CoroutineScope) -> T,
    doOut: (T) -> Unit = {},
    dispatcherIn: CoroutineDispatcher = Dispatchers.IO,
    dispatcherOut: CoroutineDispatcher = Dispatchers.Main
): Job {
    return GlobalScope.launch(dispatcherIn) {
        val data = doIn(this)
        withContext(dispatcherOut) {
            doOut(data)
        }
    }
}

private var jobLoading: Job = Job()
private var scopeDoing = CoroutineScope(Dispatchers.IO + jobLoading)
fun <T> doJob(
    doIn: (scopeDoIn: CoroutineScope) -> T,
    doOut: (T) -> Unit = {},
    dispatcherIn: CoroutineDispatcher = Dispatchers.IO,
    dispatcherOut: CoroutineDispatcher = Dispatchers.Main
) {
    jobLoading = async(doIn, doOut, dispatcherIn, dispatcherOut)
}

suspend fun doJobInThreadType(
    doJob: () -> Unit,
    threadType: CoroutineDispatcher = Dispatchers.Main
) {
    withContext(threadType) {
        doJob()
    }
}

fun cancelLoading() {
    scopeDoing.cancel()
}

fun Rect.set(left: Number, top: Number, right: Number, bottom: Number) {
    set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
}

fun RectF.set(left: Number, top: Number, right: Number, bottom: Number) {
    set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
}

fun RectF.setAppend(left: Number, top: Number, rightFromLeft: Number, bottomFromTop: Number) {
    set(
        left.toFloat(),
        top.toFloat(),
        left.toFloat() + rightFromLeft.toFloat(),
        top.toFloat() + bottomFromTop.toFloat()
    )
}

fun Rect.setAppend(left: Number, top: Number, rightFromLeft: Number, bottomFromTop: Number) {
    set(
        left.toInt(),
        top.toInt(),
        left.toInt() + rightFromLeft.toInt(),
        top.toInt() + bottomFromTop.toInt()
    )
}

fun RectF.setCenter(centerX: Number, centerY: Number, width: Number, height: Number) {
    set(
        centerX.toFloat() - width.toFloat() / 2,
        centerY.toFloat() - height.toFloat() / 2,
        centerX.toFloat() + width.toFloat() / 2,
        centerY.toFloat() + height.toFloat() / 2
    )
}

fun Rect.setCenter(centerX: Number, centerY: Number, width: Number, height: Number) {
    set(
        centerX.toInt() - width.toInt() / 2,
        centerY.toInt() - height.toInt() / 2,
        centerX.toInt() + width.toInt() / 2,
        centerY.toInt() + height.toInt() / 2
    )
}

fun Context.getWidthScreen(): Int {
    return resources.displayMetrics.widthPixels
}

fun Context.getHeightScreen(): Int {
    return resources.displayMetrics.heightPixels
}

fun Fragment.getWidthScreen(): Int {
    return resources.displayMetrics.widthPixels
}

fun Fragment.getHeightScreen(): Int {
    return resources.displayMetrics.heightPixels
}

fun Activity.getOrientation() = resources.configuration.orientation
fun Fragment.getOrientation() = resources.configuration.orientation

@SuppressLint("ClickableViewAccessibility")
fun View.setGestureMoving(
    onMoving: (distanceX: Float, distanceY: Float) -> Unit = { _, _ -> },
    onMovingVertical: (distanceY: Float) -> Unit = {},
    onMovingHorizontal: (distanceX: Float) -> Unit = {},
    onReleaseTouch: () -> Unit = {}
): GestureDetector? {
    if (context == null)
        return null
    isClickable = true
    val gestureDetector = GestureDetector(context!!, object :
        GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            val disX = -distanceX
            val disY = -distanceY
            onMoving(disX, disY)
            onMovingVertical(disY)
            onMovingHorizontal(disX)
            return true
        }
    })
    setOnTouchListener { _, event ->
        when (event?.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                onReleaseTouch()
                false
            }
            else -> {
                gestureDetector.onTouchEvent(event)
            }
        }
    }
    return gestureDetector
}

fun Activity.muteMicrophone(mute: Boolean) {
    muteMicrophone(this, mute)
}

fun Fragment.muteMicrophone(mute: Boolean) {
    context?.let {
        muteMicrophone(it, mute)
    }
}

private fun muteMicrophone(context: Context, mute: Boolean) {
    val myAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    val workingAudioMode = myAudioManager.mode

    myAudioManager.mode = AudioManager.MODE_IN_COMMUNICATION

    if (myAudioManager.isMicrophoneMute != mute) {
        myAudioManager.isMicrophoneMute = mute
    }

    myAudioManager.mode = workingAudioMode
}

fun View.onInitialized(onInit: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (isShown) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                onInit()
            }
        }
    })
}
