package com.arstagaev.currencyratetracker1.utils.extensions

import java.math.RoundingMode
import java.text.DecimalFormat

fun String.roundTo4decimals(num: String) =
    try {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        (df.format(num)).replace(",",".")
    }
    catch (e: Exception) { "NaN" }