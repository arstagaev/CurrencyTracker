package com.arstagaev.currencyratetracker1.data.remote.models

data class CurrencyPairs(
    val base: String,
    val date: String,
    val rates: Map<String, String>?,
    val success: Boolean,
    val timestamp: Int
)