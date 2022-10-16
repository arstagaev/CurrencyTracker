package com.arstagaev.currencyratetracker1.data.remote

import com.arstagaev.currencyratetracker1.data.remote.models.AvailableCurrencies
import com.arstagaev.currencyratetracker1.data.remote.models.CurrencyPairs
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("symbols")
    suspend fun getSymbols(@Query("apikey") apikey: String): AvailableCurrencies

    @GET("latest")
    suspend fun getLatest(
        @Query("apikey") apikey: String,
        @Query("base") base: String,
    ): CurrencyPairs
}