package com.moengage.sampleapp.libaries.moengage

import android.app.Application
import com.moengage.core.DataCenter
import com.moengage.core.LogLevel
import com.moengage.core.MoEngage
import com.moengage.core.config.LogConfig
import com.moengage.core.config.NotificationConfig
import com.moengage.core.config.StorageEncryptionConfig
import com.moengage.core.config.StorageSecurityConfig
import com.moengage.pushbase.MoEPushHelper
import com.moengage.sampleapp.R
import com.moengage.sampleapp.logger.log
import javax.inject.Inject

private const val MOENGAGE_WORKSPACE_ID = "YOUR_WORKSPACE_ID"

class MoEngageInitializer @Inject constructor(private val application: Application) {

    private val tag = "MoEngageInitializer"

    fun initialize() {
        log(tag) { "initialize(): " }

        MoEngage.initialiseDefaultInstance(
            application,
            MOENGAGE_WORKSPACE_ID,
            DataCenter.DATA_CENTER_1
        ) {
            configureNotificationMetaData(
                NotificationConfig(
                    smallIcon = R.drawable.icon_notification_small,
                    largeIcon = R.drawable.logo,
                    notificationColor = R.color.teal_700,
                    isMultipleNotificationInDrawerEnabled = true,
                    isBuildingBackStackEnabled = true,
                    isLargeIconDisplayEnabled = true
                )
            )
            configureStorageSecurity(StorageSecurityConfig(StorageEncryptionConfig(false)))
            configureLogs(LogConfig(LogLevel.VERBOSE, true))
        }

        MoEPushHelper.getInstance().setUpNotificationChannels(application)
    }
}