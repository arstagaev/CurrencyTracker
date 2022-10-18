package com.arstagaev.currencyratetracker1.data

import com.arstagaev.currencyratetracker1.data.local.db.CurrencyDao
import com.arstagaev.currencyratetracker1.data.local.db.models.AvailableCurrencyDto
import com.arstagaev.currencyratetracker1.data.local.db.models.CachedCurrencyPairsDto
import com.arstagaev.currencyratetracker1.data.remote.ApiRoutes
import com.arstagaev.currencyratetracker1.data.remote.CurrencyApi
import com.arstagaev.currencyratetracker1.data.remote.StatusCode
import com.arstagaev.currencyratetracker1.data.remote.models.AvailableCurrencies
import com.arstagaev.currencyratetracker1.data.remote.models.CurrencyPairs
import com.arstagaev.currencyratetracker1.ui.enums.SortState
import com.arstagaev.currencyratetracker1.utils.Resource
import com.arstagaev.currencyratetracker1.utils.logAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val currenciesDao: CurrencyDao
) {
    /**
     * Remote:
     */
    suspend fun getAvailableCurrencies(): Flow<Resource<AvailableCurrencies>> = flow {
        emit(Resource.Loading)
        try {
            val searchResult = currencyApi.getSymbols(apikey = ApiRoutes.API_KEY)
            logAction("${searchResult.toString()}")
            emit(Resource.Success(data = searchResult))

        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    suspend fun getAllPairsCurrencies(baseCurrency: String): Flow<Resource<CurrencyPairs>> = flow {
        emit(Resource.Loading)
        try {

            val searchResult = currencyApi.getLatest(base = baseCurrency, apikey = ApiRoutes.API_KEY)
            val body = searchResult.body()
            logAction("${searchResult.toString()}")

            if (body != null) {

                emit(Resource.Success(data = body))

            } else {
                var cause = StatusCode.values().find { it.code == searchResult.code() }?.name ?: StatusCode.Unknown.name

                emit(Resource.Error(causes = "Ошибка: $cause"))
            }


        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    /**
     * Local:
     */
    suspend fun setAvailableCurrencies(listOfCurrencies: ArrayList<AvailableCurrencyDto>) =
        currenciesDao.saveCurrencies(currencies = listOfCurrencies)
    suspend fun saveCurrenciesPairs(listOfPairCurrencies: ArrayList<CachedCurrencyPairsDto>) =
        currenciesDao.saveCurrenciesPairs(pairCurrencies = listOfPairCurrencies)




    suspend fun setAvailableCurrency(currency: AvailableCurrencyDto) {

        if (!currenciesDao.isExist(abbreviation_ = currency.abbreviation, name_ = currency.name)) {
            currenciesDao.saveCurrency(currency = currency)
        }

    }

    suspend fun updateBaseCurrency(abbreviation: String, isBase: Boolean) =
        currenciesDao.updateTargetCurrency(abbreviation_ = abbreviation, isBase_ = isBase)

    suspend fun updateFavCurrency(abbreviation: String, isFavorite: Boolean) =
        currenciesDao.updateFavCurrency(abbreviation_ = abbreviation, isFavorite_ = isFavorite)

    suspend fun getCachedCurrencies() = currenciesDao.getCurrencies()

    suspend fun getWholeListCurrencies(sortState: SortState) = when(sortState) {
        SortState.BY_ABBREVIATION_ASC -> currenciesDao.getWholeByAbbreviationAsc()
        SortState.BY_ABBREVIATION_DESC -> currenciesDao.getWholeByAbbreviationDesc()
        SortState.BY_VALUE_ASC -> currenciesDao.getWholeByValueAsc()
        SortState.BY_VALUE_DESC -> currenciesDao.getWholeByValueDesc()
    }

    /**
     * Sorting
     */
    suspend fun sortByAbbreviation(byAscending: Boolean) {
        if (byAscending) {
            currenciesDao.getCachedCurrencyPairsDtoSortByAscAbbreviationAsc()
        } else {
            currenciesDao.getCachedCurrencyPairsDtoSortByAscAbbreviationDesc()
        }
    }
}
