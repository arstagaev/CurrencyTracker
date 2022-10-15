package com.arstagaev.currencyratetracker1.data.local.db.models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Immutable
data class CachedCurrencyPairs(
    @PrimaryKey val id: Long,
    val name: String,
    val targetName: String,
    var value: Double,
    val isFavorite: Boolean
) {

    companion object {

        fun mock() = CachedCurrencyPairs(
            id = 0,
            name = "USD",
            targetName = "EUR",
            value = 1.0,
            isFavorite = false,
        )
    }
}