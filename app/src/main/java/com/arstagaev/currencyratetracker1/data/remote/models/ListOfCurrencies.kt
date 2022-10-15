package com.arstagaev.currencyratetracker1.data.remote.models

data class ListOfCurrencies(
    val success: Boolean,
    val symbols: Map<String, String>?
)