package com.bangla.dhonihobar.util

import java.text.NumberFormat
import java.util.Locale

object BanglaFormatter {
    private val BANGLA_DIGITS = mapOf(
        '0' to '০',
        '1' to '১',
        '2' to '২',
        '3' to '৩',
        '4' to '৪',
        '5' to '৫',
        '6' to '৬',
        '7' to '৭',
        '8' to '৮',
        '9' to '৯'
    )

    fun toBanglaNumber(value: Number): String {
        val formatted = NumberFormat.getNumberInstance(Locale.US).format(value)
        return formatted.map { BANGLA_DIGITS[it] ?: it }.joinToString("")
    }

    fun formatBanglaTaka(amount: Int): String {
        val isNegative = amount < 0
        val absAmount = Math.abs(amount)
        val banglaDigits = toBanglaNumber(absAmount)
        return if (isNegative) "-৳$banglaDigits" else "৳$banglaDigits"
    }
}
