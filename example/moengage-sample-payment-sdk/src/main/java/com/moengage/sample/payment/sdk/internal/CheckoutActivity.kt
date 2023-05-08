package com.moengage.sample.payment.sdk.internal

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.sample.payment.sdk.PaymentSDK.MOE_APP_ID
import com.moengage.sample.payment.sdk.databinding.ActivityPaymentCheckoutBinding
import com.moengage.sample.payment.sdk.databinding.LayoutTableRowItemBinding
import com.moengage.sample.payment.sdk.model.PaymentData
import com.moengage.sample.payment.sdk.model.UserData

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

        showUserData()
        setUserAttribute(userData)
        trackCheckoutLandEvent(paymentData)

        /**
         * This is For Demo purposes for event tracking.
         * */
        binding.makePayment.setOnClickListener {
            submitPayment()
        }
    }

    private fun showUserData() {
        binding.userContainer.addView(getDetailRow("Name", userData.name))
        binding.userContainer.addView(getDetailRow("Phone Number", userData.phoneNumber))
        binding.userContainer.addView(getDetailRow("Email", userData.emailId))
        binding.userContainer.addView(getDetailRow("Age", userData.age.toString()))
        binding.paymentContainer.addView(getDetailRow("Amount", paymentData.amount.toString()))
        binding.paymentContainer.addView(getDetailRow("Currency", paymentData.currency))
    }

    private fun getDetailRow(title: String, value: String): View {
        val binding = LayoutTableRowItemBinding.inflate(layoutInflater)
        binding.titleEt.text = title
        binding.valueEt.text = value
        return binding.root
    }

    private fun submitPayment() {
        Log.d(TAG, "submitPayment(): ")
        trackPaymentEvent(paymentData)
        Snackbar.make(binding.root, "Payment Successful", Snackbar.LENGTH_LONG)
            .setAction("Okay") {
                finish()
            }
            .show()
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

    private fun setUserAttribute(userData: UserData) {
        val userAttributes = hashMapOf<String, Any>(
            "name" to userData.name,
            "age" to userData.age
        )

        MoEAnalyticsHelper.setUserAttribute(this, userAttributes, appId = MOE_APP_ID)
        MoEAnalyticsHelper.setEmailId(this, userData.emailId, appId = MOE_APP_ID)
        MoEAnalyticsHelper.setUserName(this, userData.name, appId = MOE_APP_ID)
        MoEAnalyticsHelper.setLocation(this, 20.550, 40.4001, appId = MOE_APP_ID)

        /**
         * This Attribute Is can be  used as unique identifier for the APP to segment users.
         * So campaigns can be launched for the users with Attribute PaymentSDK App Id = <APP_ID>
         * */
        MoEAnalyticsHelper.setUserAttribute(this, "paymentSDKAppId", "")
    }
}