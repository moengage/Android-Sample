package com.moengage.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.inapp.MoEInAppHelper
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // track event
        val properties = Properties()
        properties.addAttribute("attributeString", "string")
            .addAttribute("attributeInteger", 123)
            .addAttribute("attributeDate", Date())
            .addDateIso("attributeDateIso", "2022-02-10T21:12:00Z")
        MoEAnalyticsHelper.trackEvent(applicationContext, "EVENT_SAMPLE", properties)

        // user attribute tracking
        MoEAnalyticsHelper.setFirstName(applicationContext, "First Name")
        MoEAnalyticsHelper.setLastName(applicationContext, "Last Name")
        MoEAnalyticsHelper.setBirthDate(applicationContext, Date())
    }

    override fun onStart() {
        super.onStart()
        MoEInAppHelper.getInstance().showInApp(applicationContext)
    }
}