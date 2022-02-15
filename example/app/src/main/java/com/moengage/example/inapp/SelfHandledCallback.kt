package com.moengage.example.inapp

import com.moengage.inapp.listeners.SelfHandledAvailableListener
import com.moengage.inapp.model.SelfHandledCampaignData
import logcat.logcat

/**
 * @author Umang Chamaria
 * Date: 2022/02/03
 */
class SelfHandledCallback: SelfHandledAvailableListener {

    override fun onSelfHandledAvailable(data: SelfHandledCampaignData?) {
        logcat { " onSelfHandledAvailable() $data" }
    }
}