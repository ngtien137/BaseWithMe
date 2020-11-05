package com.base.baselibrary.utils.audio_focus

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.base.baselibrary.utils.getApplication
import com.base.baselibrary.views.ext.loge

class AudioHelper(onAudioLoss: (Boolean) -> Unit) {
    private lateinit var request: AudioFocusRequest

    private val mAudioManager by lazy {
        getApplication().getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private val mTelephonyManager by lazy {
        getApplication().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    /**
     * Ngừng lắng nghe tín hiệu media
     */
    fun stopRequestAudio() {
        try {
            abandonMediaFocus()
        } catch (e: Exception) {
        }
    }

    /**
     * Yêu cầu ngừng media các app khác
     */
    fun requestAudio(): Boolean {
        return try {
            requestAudioFocus() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Tắt âm thanh từ app khác & bắt sự kiện âm thanh
     */
    private fun requestAudioFocus(): Int {
        abandonMediaFocus()
        mTelephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE)
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
            mAudioManager.requestAudioFocus(
                    request
            )
        } else {
            @Suppress("DEPRECATION")
            mAudioManager.requestAudioFocus(
                    afListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    /**
     * Ngừng bắt sự kiện âm thanh
     */
    private fun abandonMediaFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && ::request.isInitialized) {
            mAudioManager.abandonAudioFocusRequest(request)
        } else {
            @Suppress("DEPRECATION")
            mAudioManager.abandonAudioFocus(afListener)
        }
        mTelephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE)
    }

    /**
     * Xử lý khi video được khôi phục hoặc bị mất
     */
    private val afListener = AudioManager.OnAudioFocusChangeListener { i: Int ->
        when (i) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                //Âm thanh đã được khôi phục trở lại
                // -> có thể play media
                onAudioLoss.invoke(false)
            }
            AudioManager.AUDIOFOCUS_LOSS, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                //Tạm thời mất âm thanh, vẫn có thể giảm âm lượng và play hoặc tạm ngưng âm thanh
                //lowerVolume() ||  pause()
                onAudioLoss.invoke(true)
            }
        }
    }

    /**
     * Xử lý khi có cuộc gọi
     */
    private val phoneListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            if (state != TelephonyManager.CALL_STATE_IDLE) {
                onAudioLoss.invoke(true)
            } else {
                onAudioLoss.invoke(false)
            }
            super.onCallStateChanged(state, phoneNumber)
        }
    }
}