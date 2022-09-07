package com.moengage.example

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.moengage.push.amp.plus.MiPushHelper
import com.xiaomi.channel.commonutils.android.Region
import logcat.logcat

/**
 * @author Umang Chamaria
 * Date: 2022/09/02
 */
class ApplicationLifecycleObserver(private val context: Context): DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        logcat { "application coming to foreground." }
        MiPushHelper.initialiseMiPush(
            context,
            appKey = "<your-app-key>",
            appId = "<your-app-id>",
            // replace the region with the actual region.
            Region.Global
        )
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }
}