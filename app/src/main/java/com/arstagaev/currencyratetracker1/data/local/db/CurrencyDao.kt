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

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveCurrencies(currencies: List<AvailableCurrencyDto>)


  /**
    add or update available currency
   */
  @Query("SELECT EXISTS(SELECT * FROM AvailableCurrencyDto WHERE :abbreviation_ == abbreviation AND :name_ == name)")
  suspend fun isExist(abbreviation_: String, name_: String): Boolean

  // update, WITHOUT change flags: isTarget, isFavorite
  @Query("UPDATE AvailableCurrencyDto SET abbreviation = :abbreviation_ , name = :name_ WHERE abbreviation = :abbreviation_")
  fun saveCurrencyWithPartialUpdate(abbreviation_ : String, name_: String): Int?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveCurrency(currency: AvailableCurrencyDto)


  /**
   * Update Bool Flags: isBase, isFavorite
   */
  @Query("UPDATE AvailableCurrencyDto SET isBase = :isBase_ WHERE abbreviation == :abbreviation_ ")
  suspend fun updateTargetCurrency(abbreviation_: String, isBase_: Boolean): Int?

  @Query("UPDATE AvailableCurrencyDto SET isFavorite = :isFavorite_ WHERE abbreviation == :abbreviation_ ")
  suspend fun updateFavCurrency(abbreviation_: String, isFavorite_: Boolean): Int?


  /**
   * Get Base Currency
   */
  @Query("SELECT * FROM AvailableCurrencyDto WHERE isBase = 'true'")
  suspend fun getCurrencyBase(): AvailableCurrencyDto


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
  @Query("SELECT * FROM CachedCurrencyPairsDto")
  suspend fun getCurrencyPairsCached(): CachedCurrencyPairsDto?

  @Query("SELECT * FROM AvailableCurrencyDto")
  suspend fun getCurrencies(): List<AvailableCurrencyDto>?


  /**
   * Get Cell by Filter
   */
  @Query("SELECT * FROM AvailableCurrencyDto WHERE abbreviation = :abbreviation_")
  suspend fun getCurrencyById(abbreviation_: String): AvailableCurrencyDto?

  @Query("SELECT * FROM AvailableCurrencyDto WHERE name = :name_")
  suspend fun getCurrencyByName(name_: String): AvailableCurrencyDto?

  @Query("SELECT * FROM AvailableCurrencyDto WHERE isBase = 1")
  suspend fun getTargetCurrency(): AvailableCurrencyDto?


  /**
   * Sort
   */
  @Query("SELECT * FROM CachedCurrencyPairsDto ORDER BY abbreviation DESC")
  suspend fun getCachedCurrencyPairsDtoSortByAscAbbreviationDesc(): List<CachedCurrencyPairsDto?>?

  @Query("SELECT * FROM CachedCurrencyPairsDto ORDER BY abbreviation ASC")
  suspend fun getCachedCurrencyPairsDtoSortByAscAbbreviationAsc(): List<CachedCurrencyPairsDto?>?


  /**
   * Delete
   */

  @Query("DELETE FROM CachedCurrencyPairsDto")
  suspend fun deleteCachePairs()

  @Query("DELETE FROM AvailableCurrencyDto")
  suspend fun deleteCurrencies()
}
