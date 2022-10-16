package com.arstagaev.currencyratetracker1.data.remote.models

data class AvailableCurrencies(
    val success: Boolean,
    val symbols: Map<String, String>?
)