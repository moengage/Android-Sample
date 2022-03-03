package com.moengage.micro_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.inapp.MoEInAppHelper
import java.util.*

class MicroAppHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_micro_app_home)

        // track event
        val properties = Properties()
        properties.addAttribute("attributeString", "string")
            .addAttribute("attributeInteger", 123)
            .addAttribute("attributeDate", Date())
            .addDateIso("attributeDateIso", "2022-02-10T21:12:00Z")
        MoEAnalyticsHelper.trackEvent(applicationContext, "EVENT_SAMPLE", properties, MOENGAGE_APP_ID)

        // user attribute tracking
        MoEAnalyticsHelper.setFirstName(applicationContext, "First Name", MOENGAGE_APP_ID)
        MoEAnalyticsHelper.setLastName(applicationContext, "", MOENGAGE_APP_ID)
        MoEAnalyticsHelper.setBirthDate(applicationContext, Date(), MOENGAGE_APP_ID)
    }

    override fun onStart() {
        super.onStart()
        MoEInAppHelper.getInstance().showInApp(applicationContext, MOENGAGE_APP_ID)
    }
}