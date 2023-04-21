package com.example.mpay

import android.util.Log
import com.moengage.inapp.listeners.InAppLifeCycleListener
import com.moengage.inapp.model.InAppData

class MPayInAppLifeCycleListener : InAppLifeCycleListener {

    override fun onDismiss(inAppData: InAppData) {
        Log.d("M_Pay", "InAppDismissed: $inAppData")

    }

    override fun onShown(inAppData: InAppData) {
        Log.d("M_Pay", "InAppShown: $inAppData")
    }
}