package com.moengage.sample.payment.sdk

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.moengage.sample.payment.sdk.PaymentSDK.MOE_APP_ID
import com.moengage.cards.ui.CardActivity
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.inapp.MoEInAppHelper
import com.moengage.sample.payment.sdk.databinding.ActivityPaymentCheckoutBinding

internal class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentCheckoutBinding

    private val TAG: String = "CheckoutActivity"

    private lateinit var userData: UserData

    private lateinit var paymentData: PaymentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.getSerializable(INTENT_KEY_USER_DATA, UserData::class.java)
        paymentData = intent.getSerializable(INTENT_KEY_PAYMENT_DATA, PaymentData::class.java)

        setUserAttribute(userData)
        trackCheckoutLandEvent(paymentData)


        binding.nudge.initialiseNudgeView(this, MOE_APP_ID)

        /**
         * This is For Demo purposes for event tracking.
         * */
        binding.makePayment.setOnClickListener {
            submitPayment()
        }
    }

    private fun submitPayment() {
        Log.d(TAG, "submitPayment(): ")
        trackPaymentEvent(paymentData)
    }

    private fun trackPaymentEvent(paymentData: PaymentData) {
        val properties = Properties()
        properties.addAttribute("amount", paymentData.amount)
        properties.addAttribute("currency", paymentData.currency)
        properties.addAttribute("productId", "ABCDEF")
        MoEAnalyticsHelper.trackEvent(this, "payment", properties, MOE_APP_ID)
    }

    private fun trackCheckoutLandEvent(paymentData: PaymentData) {
        val properties = Properties()
        properties.addAttribute("amount", paymentData.amount)
        properties.addAttribute("currency", paymentData.currency)
        MoEAnalyticsHelper.trackEvent(this, "checkout", properties, MOE_APP_ID)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        MoEInAppHelper.getInstance().onConfigurationChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.checkout_menu, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.notifications) {
            /**
             * Here we are navigating to default cards UI implementation
             * */
            val intent = Intent(this, CardActivity::class.java)
            intent.putExtra(INTENT_KEY_APPID, MOE_APP_ID)
            startActivity(intent)
        }
        return true
    }

    private fun setUserAttribute(userData: UserData) {
        val userAttributes = hashMapOf<String, Any>(
            "name" to userData.name,
            "age" to 30
        )

        MoEAnalyticsHelper.setUserAttribute(this, userAttributes, appId = MOE_APP_ID)
        MoEAnalyticsHelper.setEmailId(this, userData.emailId, appId = MOE_APP_ID)
        MoEAnalyticsHelper.setUserName(this, userData.name, appId = MOE_APP_ID)
        MoEAnalyticsHelper.setLocation(this, 20.550, 40.4001, appId = MOE_APP_ID)

        /**
         * This Attribute Is can be  used as unique identifier for the APP to segment users.
         * So campaigns can be launched for the users with Attribute PaymentSDK App Id = <APP_ID>
         * */
        MoEAnalyticsHelper.setUserAttribute(this, "paymentSDKAppId", PaymentSDK.paymentSDKAppId)
    }
}