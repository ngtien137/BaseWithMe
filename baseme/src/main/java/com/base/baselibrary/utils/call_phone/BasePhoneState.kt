package com.base.baselibrary.utils.call_phone

import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.lifecycle.MutableLiveData

object BasePhoneState {

    val livePhoneState = MutableLiveData(State.STATE_NONE)

    private val phoneState by lazy {
        PhoneState(object :
            PhoneState.IPhoneState {
            override fun callInComing() {
                livePhoneState.value =
                    State.STATE_INCOMING
            }

            override fun callIdle() {
                livePhoneState.value =
                    State.STATE_IDLE
            }
        })
    }

    fun listenPhoneState(context: Context) {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(phoneState, PhoneStateListener.LISTEN_CALL_STATE)
    }

    enum class State {
        STATE_INCOMING, STATE_NONE, STATE_IDLE
    }

}