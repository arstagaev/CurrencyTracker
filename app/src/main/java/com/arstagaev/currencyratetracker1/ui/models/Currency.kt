package com.arstagaev.currencyratetracker1.ui.models

data class Currency(
    val abbreviation: String,
    val name: String,
    var isTarget: Boolean  = false,
    var isFavorite: Boolean = false,
    var value: Double
)