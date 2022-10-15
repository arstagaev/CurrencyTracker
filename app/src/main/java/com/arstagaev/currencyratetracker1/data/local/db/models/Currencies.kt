package com.arstagaev.currencyratetracker1.data.local.db.models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Immutable
data class Currencies(
    @PrimaryKey val id: Long,
    val name: String,
    var isTarget: Boolean,
    var isFavorite: Boolean
) {

    companion object {

        fun mock() = Currencies(
            id = 0,
            name = "USD",
            isTarget = false,
            isFavorite = false
        )
    }
}