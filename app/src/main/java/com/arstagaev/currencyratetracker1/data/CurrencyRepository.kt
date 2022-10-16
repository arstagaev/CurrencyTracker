package com.arstagaev.currencyratetracker1.data

import com.arstagaev.currencyratetracker1.data.local.db.CurrencyDao
import com.arstagaev.currencyratetracker1.data.local.db.models.AvailableCurrencyDto
import com.arstagaev.currencyratetracker1.data.local.db.models.CachedCurrencyPairsDto
import com.arstagaev.currencyratetracker1.data.remote.ApiRoutes
import com.arstagaev.currencyratetracker1.data.remote.CurrencyApi
import com.arstagaev.currencyratetracker1.data.remote.models.AvailableCurrencies
import com.arstagaev.currencyratetracker1.data.remote.models.CurrencyPairs
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
            logAction("${searchResult.toString()}")
            emit(Resource.Success(data = searchResult))

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

    suspend fun updateTargetCurrency(abbreviation: String, isTarget: Boolean) {
        currenciesDao.updateTargetCurrency(abbreviation_ = abbreviation, isTarget_ = isTarget)
    }

    suspend fun getCachedCurrencies() = currenciesDao.getCurrencies()

    suspend fun getWholeListCurrencies() = currenciesDao.getWhole()
}
