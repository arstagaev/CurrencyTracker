
package com.arstagaev.currencyratetracker1.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arstagaev.currencyratetracker1.data.local.db.models.CachedCurrencyPairsDto
import com.arstagaev.currencyratetracker1.data.local.db.models.AvailableCurrencyDto

@Database(entities = [
  AvailableCurrencyDto::class,
  CachedCurrencyPairsDto::class
], version = 1, exportSchema = true)

abstract class AppDatabase : RoomDatabase() {

  abstract fun currencyDao(): CurrencyDao
}
