package com.example.mpay

data class PaymentData(
    val amount: Int,
    val currency: String
) : java.io.Serializable
