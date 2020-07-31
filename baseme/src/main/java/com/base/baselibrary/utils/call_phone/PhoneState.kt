package com.base.baselibrary.utils.call_phone

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager

class PhoneState(private var iPhoneState: IPhoneState) : PhoneStateListener() {

    override fun onCallStateChanged(state: Int, incomingNumber: String) {
        super.onCallStateChanged(state, incomingNumber)
        if (state == TelephonyManager.CALL_STATE_RINGING) {
            iPhoneState.callInComing()
        }else if (state == TelephonyManager.CALL_STATE_IDLE){
            iPhoneState.callIdle()
        }

    }

    interface IPhoneState {
        fun callInComing()
        fun callIdle()
    }
}
