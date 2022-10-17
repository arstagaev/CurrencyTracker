package com.arstagaev.currencyratetracker1.data.local.db.models

data class CurrencyDto(
    val abbreviation: String,
    val name: String,
    var isBase: Boolean  = false,
    var isFavorite: Boolean = false,
    var value: Double
)
