
package com.arstagaev.currencyratetracker1.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arstagaev.currencyratetracker1.data.local.db.models.CachedCurrencyPairs
import com.arstagaev.currencyratetracker1.data.local.db.models.Currencies

@Database(entities = [Currencies::class, CachedCurrencyPairs::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

  abstract fun currencyDao(): CurrencyDao
}
