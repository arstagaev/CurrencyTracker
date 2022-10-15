package com.arstagaev.currencyratetracker1.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arstagaev.currencyratetracker1.data.local.db.models.CachedCurrencyPairs
import com.arstagaev.currencyratetracker1.data.local.db.models.Currencies

@Dao
interface CurrencyDao {

  // set with replace

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveCurrenciesPairs(pairCurrencies: List<CachedCurrencyPairs>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveCurrencies(currencies: List<Currencies>)

  // get whole

  @Query("SELECT * FROM CachedCurrencyPairs")
  suspend fun getCurrencyPairsCached(): CachedCurrencyPairs?

  @Query("SELECT * FROM Currencies")
  suspend fun getCurrencies(): Currencies?

  // get with filter

  @Query("SELECT * FROM Currencies WHERE id = :id_")
  suspend fun getCurrencyById(id_: Long): Currencies?

  @Query("SELECT * FROM Currencies WHERE name = :name_")
  suspend fun getCurrencyByName(name_: String): Currencies?

  @Query("SELECT * FROM Currencies WHERE isTarget = 1")
  suspend fun getTargetCurrency(): Currencies?



  // delete

  @Query("DELETE FROM CachedCurrencyPairs")
  suspend fun deleteCachePairs()

  @Query("DELETE FROM Currencies")
  suspend fun deleteCurrencies()
}
