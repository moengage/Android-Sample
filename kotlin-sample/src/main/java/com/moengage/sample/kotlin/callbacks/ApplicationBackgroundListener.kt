package com.moengage.sample.kotlin.callbacks

import android.content.Context
import com.moengage.core.listeners.AppBackgroundListener
import timber.log.Timber

/**
 * @author Umang Chamaria
 * Date: 2019-05-31
 */
class ApplicationBackgroundListener : AppBackgroundListener {

    override fun onAppBackground(context: Context) {
        Timber.v(" goingToBackground(): Application going to background callback received.")
        // application going to background, add your logic here.
    }
}