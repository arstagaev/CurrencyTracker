package com.arstagaev.currencyratetracker1.data

import com.arstagaev.currencyratetracker1.data.local.db.CurrencyDao
import com.arstagaev.currencyratetracker1.data.remote.ApiRoutes
import com.arstagaev.currencyratetracker1.data.remote.CurrencyApi
import com.arstagaev.currencyratetracker1.data.remote.models.ListOfCurrencies
import com.arstagaev.currencyratetracker1.utils.Resource
import com.arstagaev.currencyratetracker1.utils.logAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val currenciesDao: CurrencyDao
) {

    suspend fun getAvailableCurrencies(): Flow<Resource<ListOfCurrencies>> = flow {
        emit(Resource.Loading)
        try {
            val searchResult = currencyApi.getSymbols(apikey = ApiRoutes.API_KEY)
            logAction("${searchResult.toString()}")
            emit(Resource.Success(data = searchResult))

        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}