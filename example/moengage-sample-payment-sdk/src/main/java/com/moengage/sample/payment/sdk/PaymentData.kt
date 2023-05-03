package com.moengage.sample.payment.sdk

import java.io.Serializable

data class PaymentData(
    val amount: Int,
    val currency: String
) : Serializable