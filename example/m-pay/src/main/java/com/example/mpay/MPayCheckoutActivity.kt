package com.example.mpay

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.mpay.MPay.MOE_APP_ID
import com.example.mpay.MPay.MOE_APP_ID_KEY
import com.example.mpay.databinding.ActivityMpayCheckoutBinding
import com.moengage.cards.ui.CardActivity
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.inapp.MoEInAppHelper

internal class MPayCheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMpayCheckoutBinding

    private val TAG: String = "M_Pay_CheckoutActivity"

    private lateinit var userData: UserData

    private lateinit var paymentData: PaymentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMpayCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.getSerializable("userData", UserData::class.java)
        paymentData = intent.getSerializable("paymentData", PaymentData::class.java)
        setUserAttribute(userData)
        trackPaymentEvent(paymentData)
        binding.nudge.initialiseNudgeView(this, MOE_APP_ID)

        setInAppListener()
        MoEInAppHelper.getInstance().showInApp(this, MOE_APP_ID)
        MoEInAppHelper.getInstance().getSelfHandledInApp(this, MOE_APP_ID) {
            Log.d(TAG, it.toString())
        }

        /**
         *This is For Demo purposes, Before calling this, app should have play services dependency, location permission given my user,location currently enabled.
         * */
/*
        binding.startGeofence.setOnClickListener {
            MoEGeofenceHelper.getInstance().startGeofenceMonitoring(this, MOE_APP_ID)
        }

        binding.stopGeofence.setOnClickListener {
            MoEGeofenceHelper.getInstance().startGeofenceMonitoring(this, MOE_APP_ID)
        }
*/

        /**
         * This is For Demo purposes. We can create event triggered / device trigger campaign from dashboard with below event name.
         * So when user performs the below event, notification can be shown to user.
         * */
        binding.makePayment.setOnClickListener {
            val properties = Properties()
            properties.addAttribute("paymentAmount", 1000)
            properties.addAttribute("productId", "ABCDEF")
            MoEAnalyticsHelper.trackEvent(this, "payment", properties, MOE_APP_ID)

        }
    }

    private fun trackPaymentEvent(paymentData: PaymentData) {
        val properties = Properties()
        properties.addAttribute("amount", paymentData.amount)
        properties.addAttribute("currency", paymentData.currency)
        MoEAnalyticsHelper.trackEvent(this, "payment", properties, MOE_APP_ID)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        MoEInAppHelper.getInstance().onConfigurationChanged()
    }


    private fun setInAppListener() {
        MoEInAppHelper.getInstance()
            .addInAppLifeCycleListener(MOE_APP_ID, MPayInAppLifeCycleListener())
        MoEInAppHelper.getInstance().setClickActionListener(MOE_APP_ID) { clickData ->
            Log.d(TAG, clickData.toString())
            false
        }
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
            intent.putExtra(MOE_APP_ID_KEY, MOE_APP_ID)
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
         * So campaigns can be launched for the users with Attribute mPayAppId = <APP_ID>
         * */
        MoEAnalyticsHelper.setUserAttribute(this, "mPayAppId", MPay.MPAY_APP_ID)
    }
}