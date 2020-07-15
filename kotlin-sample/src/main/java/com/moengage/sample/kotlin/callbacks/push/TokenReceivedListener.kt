package com.moengage.sample.kotlin.callbacks.push

import com.moengage.push.PushManager
import timber.log.Timber

/**
 * @author Umang Chamaria
 * Date: 2019-05-31
 */
class TokenReceivedListener: PushManager.OnTokenReceivedListener {
  override fun onTokenReceived(token: String) {
    Timber.v("onTokenReceived(): Token Callback Received. Token: $token")
    // push token received, add your processing logic here
  }
}