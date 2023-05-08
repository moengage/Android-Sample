package com.moengage.sample.payment.sdk.model

import java.io.Serializable

data class PaymentData(
    val amount: Double,
    val currency: String
) : Serializable