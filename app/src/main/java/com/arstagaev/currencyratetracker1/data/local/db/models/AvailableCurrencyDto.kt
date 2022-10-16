package com.arstagaev.currencyratetracker1.data.local.db.models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Immutable
data class AvailableCurrencyDto(
    @PrimaryKey val abbreviation: String,
    val name: String,
    var isBase: Boolean  = false,
    var isFavorite: Boolean = false
)
