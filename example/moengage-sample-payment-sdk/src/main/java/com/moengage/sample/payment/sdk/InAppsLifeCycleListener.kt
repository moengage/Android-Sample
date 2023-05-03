package com.moengage.sample.payment.sdk

import android.util.Log
import com.moengage.inapp.listeners.InAppLifeCycleListener
import com.moengage.inapp.model.InAppData
import com.moengage.sample.payment.sdk.PaymentSDK.TAG

class InAppsLifeCycleListener : InAppLifeCycleListener {

    override fun onDismiss(inAppData: InAppData) {
        Log.d(TAG, "InAppDismissed: $inAppData")
    }

    override fun onShown(inAppData: InAppData) {
        Log.d(TAG, "InAppShown: $inAppData")
    }
}