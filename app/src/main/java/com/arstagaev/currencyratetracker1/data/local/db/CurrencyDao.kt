package com.arstagaev.currencyratetracker1.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arstagaev.currencyratetracker1.data.local.db.models.AvailableCurrencyDto
import com.arstagaev.currencyratetracker1.data.local.db.models.CachedCurrencyPairsDto
import com.arstagaev.currencyratetracker1.data.local.db.models.CurrencyDto

@Dao
interface CurrencyDao {

  /**
   * Save from server
   */

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveCurrenciesPairs(pairCurrencies: List<CachedCurrencyPairsDto>)


  /**
    add or update available currency
   */
  @Query("SELECT EXISTS(SELECT * FROM AvailableCurrencyDto WHERE :abbreviation_ == abbreviation AND :name_ == name)")
  suspend fun isExist(abbreviation_: String, name_: String): Boolean

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveCurrency(currency: AvailableCurrencyDto)


  /**
   * Update Bool Flags: isBase, isFavorite
   */

  @Query("UPDATE AvailableCurrencyDto SET isFavorite = :isFavorite_ WHERE abbreviation == :abbreviation_ ")
  suspend fun updateFavCurrency(abbreviation_: String, isFavorite_: Boolean): Int?


  /**
   * Get Whole List with Custom Sort
   */
  @Query("SELECT * FROM AvailableCurrencyDto, CachedCurrencyPairsDto WHERE AvailableCurrencyDto.abbreviation = CachedCurrencyPairsDto.abbreviation ORDER BY abbreviation ASC")
  suspend fun getWholeByAbbreviationAsc(): List<CurrencyDto>?

  @Query("SELECT * FROM AvailableCurrencyDto, CachedCurrencyPairsDto WHERE AvailableCurrencyDto.abbreviation = CachedCurrencyPairsDto.abbreviation ORDER BY abbreviation DESC")
  suspend fun getWholeByAbbreviationDesc(): List<CurrencyDto>?

  @Query("SELECT * FROM AvailableCurrencyDto, CachedCurrencyPairsDto WHERE AvailableCurrencyDto.abbreviation = CachedCurrencyPairsDto.abbreviation ORDER BY value ASC")
  suspend fun getWholeByValueAsc(): List<CurrencyDto>?

  @Query("SELECT * FROM AvailableCurrencyDto, CachedCurrencyPairsDto WHERE AvailableCurrencyDto.abbreviation = CachedCurrencyPairsDto.abbreviation ORDER BY value DESC")
  suspend fun getWholeByValueDesc(): List<CurrencyDto>?

  /**
   *  Get Target Table
   */

  @Query("SELECT * FROM AvailableCurrencyDto")
  suspend fun getCurrencies(): List<AvailableCurrencyDto>?

  /**
   * Delete
   */

  @Query("DELETE FROM CachedCurrencyPairsDto")
  suspend fun deleteCachePairs()

  @Query("DELETE FROM AvailableCurrencyDto")
  suspend fun deleteCurrencies()
}
