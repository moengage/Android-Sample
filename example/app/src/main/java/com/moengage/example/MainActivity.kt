package com.moengage.example

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.moengage.sample.payment.sdk.PaymentData
import com.moengage.sample.payment.sdk.PaymentSDK
import com.moengage.sample.payment.sdk.UserData
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
        properties.addAttribute("attributeString", "string").addAttribute("attributeInteger", 123)
            .addAttribute("attributeDate", Date())
            .addDateIso("attributeDateIso", "2022-02-10T21:12:00Z")
        MoEAnalyticsHelper.trackEvent(applicationContext, "EVENT_SAMPLE", properties)

        // user attribute tracking
        MoEAnalyticsHelper.setFirstName(applicationContext, "First Name")
        MoEAnalyticsHelper.setLastName(applicationContext, "Last Name")
        MoEAnalyticsHelper.setBirthDate(applicationContext, Date())

        findViewById<Button>(R.id.button).setOnClickListener {
            checkoutPayment()
        }
    }

    /**
     * For Demo Purposes, we are navigating to Payment Checkout by passing user and payment details
     * */
    private fun checkoutPayment() {
        val userData = UserData(
            "Alice", "1234", "a@a.com", 23,
            latLocation = 12.999, longLocation = 21.0
        )
        val paymentData = PaymentData(2000, "INR")
        PaymentSDK.checkoutPayment(this, userData = userData, paymentData = paymentData)
    }

    override fun onStart() {
        super.onStart()
        MoEInAppHelper.getInstance().showInApp(applicationContext)
    }
}