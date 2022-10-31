package com.arstagaev.currencyratetracker1.data.remote

import com.arstagaev.currencyratetracker1.data.remote.models.AvailableCurrencies
import com.arstagaev.currencyratetracker1.data.remote.models.CurrencyPairs
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface CurrencyApi {

    // get available currencies
    @GET("symbols")
    suspend fun getSymbols(@Query("apikey") apikey: String): AvailableCurrencies

    // get latest rate for currency pair
    @GET("latest")
    suspend fun getLatest(
        @Query("apikey") apikey: String,
        @Query("base") base: String,
    ): Response<CurrencyPairs>
}
