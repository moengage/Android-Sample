package com.moengage.sampleapp.ui.util

/** ₹1,240 — grouped Indian-style, which for these amounts is the same as plain grouping. */
fun rupees(amount: Int): String {
    val digits = amount.toString()
    if (digits.length <= 3) return "₹$digits"
    val head = digits.dropLast(3)
    val tail = digits.takeLast(3)
    val grouped = head.reversed().chunked(2).joinToString(",").reversed()
    return "₹$grouped,$tail"
}

/** "+₹20" / "" for a surcharge. */
fun surcharge(amount: Int): String = if (amount == 0) "" else " +₹$amount"
