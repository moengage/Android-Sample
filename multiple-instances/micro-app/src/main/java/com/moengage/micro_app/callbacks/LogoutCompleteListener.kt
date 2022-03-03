package com.moengage.micro_app.callbacks

import com.moengage.core.listeners.OnLogoutCompleteListener
import com.moengage.core.model.LogoutData
import logcat.logcat

/**
 * @author Umang Chamaria
 * Date: 2022/02/15
 */
class LogoutCompleteListener: OnLogoutCompleteListener {
    override fun logoutComplete(data: LogoutData) {
        logcat { "logoutComplete() $data" }
    }
}