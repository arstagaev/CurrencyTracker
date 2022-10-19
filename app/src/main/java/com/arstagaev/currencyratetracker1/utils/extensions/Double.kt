package com.arstagaev.currencyratetracker1.utils.extensions

import java.lang.Double.NaN
import java.math.RoundingMode
import java.text.DecimalFormat

fun Double.roundTo4decimals() =
    try {
        val df = DecimalFormat("#.####")
        df.roundingMode = RoundingMode.CEILING
        (df.format(this)).replace(",",".")
    }
    catch (e: Exception) { NaN }