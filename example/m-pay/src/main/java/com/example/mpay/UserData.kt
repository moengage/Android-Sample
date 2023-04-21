package com.example.mpay

import java.io.Serializable


data class UserData(
    val name: String,
    val phoneNumber: String,
    val emailId: String,
    val age: Int,
    val latLocation: Double,
    val longLocation: Double
) : Serializable