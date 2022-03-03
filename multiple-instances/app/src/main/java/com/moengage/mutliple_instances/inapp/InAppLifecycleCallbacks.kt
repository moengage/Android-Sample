package com.moengage.mutliple_instances.inapp

import com.moengage.inapp.listeners.InAppLifeCycleListener
import com.moengage.inapp.model.InAppData
import logcat.logcat

/**
 * @author Umang Chamaria
 * Date: 2022/02/03
 */
class InAppLifecycleCallbacks: InAppLifeCycleListener {

    override fun onDismiss(inAppData: InAppData) {
        logcat { " onDismiss() Data: $inAppData" }
    }

    override fun onShown(inAppData: InAppData) {
        logcat { " onShown() Data: $inAppData" }
    }
}