package com.moengage.example

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.moengage.core.MoECoreHelper
import com.moengage.core.MoEngage
import com.moengage.core.config.*
import com.moengage.core.ktx.MoEngageBuilderKtx
import com.moengage.example.callbacks.ApplicationBackgroundListener
import com.moengage.example.callbacks.CustomPreProcessingListener
import com.moengage.example.callbacks.LogoutCompleteListener
import com.moengage.example.inapp.ClickActionCallback
import com.moengage.example.inapp.InAppLifecycleCallbacks
import com.moengage.example.inapp.SelfHandledCallback
import com.moengage.example.push.CustomPushMessageListener
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.inapp.MoEInAppHelper
import com.moengage.pushbase.MoEPushHelper
import logcat.logcat

/**
 * @author Umang Chamaria
 * Date: 2022/02/03
 */
class MoEngageDemoApplication: Application() {

    private val tag = "MoEngageDemoApplication"

    override fun onCreate() {
        super.onCreate()
        application = this
        // Register the pre-processing listener, triggered when SDK tries to process intent before initialisation
        // Have this in onCreate() only
        MoECoreHelper.registerPreProcessingListener("YOUR_APP_ID", CustomPreProcessingListener())
        MoEngageHandler().initialise(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            ApplicationLifecycleObserver(
                applicationContext
            )
        )
    }

    companion object {

        private lateinit var application: Application
        fun getApplication(): Application {
            return application
        }
    }

}