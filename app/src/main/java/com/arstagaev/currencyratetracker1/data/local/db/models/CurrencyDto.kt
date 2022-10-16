package com.arstagaev.currencyratetracker1.data.local.db.models

data class CurrencyDto(
    val abbreviation: String,
    val name: String,
    var isTarget: Boolean  = false,
    var isFavorite: Boolean = false,
    var value: Double
)
