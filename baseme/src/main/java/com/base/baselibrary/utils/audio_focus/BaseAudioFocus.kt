package com.base.baselibrary.utils.audio_focus

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.base.baselibrary.utils.getApplication

object BaseAudioFocus {
    private var mAudioManager: AudioManager? = null
    private lateinit var request: AudioFocusRequest
    var liveAudioState = MutableLiveData(State.STATE_AUDIO_NONE)
    private val afListener = AudioManager.OnAudioFocusChangeListener { i: Int ->
        when (i) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                liveAudioState.postValue(State.STATE_AUDIO_GAIN)
            }
            AudioManager.AUDIOFOCUS_LOSS, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                liveAudioState.postValue(State.STATE_AUDIO_LOSS)
            }
        }
    }

    fun init() {
        mAudioManager = getApplication().getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    fun requestAudioFocus(): Int {
        abandonMediaFocus()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //
            request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(afListener)
                .build()
            //
            mAudioManager!!.requestAudioFocus(
                request
            )
        } else {
            @Suppress("DEPRECATION")
            mAudioManager!!.requestAudioFocus(
                afListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    fun abandonMediaFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && ::request.isInitialized) {
            mAudioManager!!.abandonAudioFocusRequest(request)
        } else {
            @Suppress("DEPRECATION")
            mAudioManager!!.abandonAudioFocus(afListener)
        }
    }

    enum class State {
        STATE_AUDIO_NONE, STATE_AUDIO_GAIN, STATE_AUDIO_LOSS
    }
}