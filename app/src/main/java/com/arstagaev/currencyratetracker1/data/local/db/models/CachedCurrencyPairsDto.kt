package com.arstagaev.currencyratetracker1.data.local.db.models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Immutable
data class CachedCurrencyPairsDto(
    @PrimaryKey val abbreviation: String,
    var value: Double
) {

    companion object {

        fun mock() = CachedCurrencyPairsDto(
            abbreviation = "USD",
            value = 1.0
        )
    }
}