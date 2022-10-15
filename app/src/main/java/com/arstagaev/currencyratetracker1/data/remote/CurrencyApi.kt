package com.arstagaev.currencyratetracker1.data.remote

import com.arstagaev.currencyratetracker1.data.remote.models.ListOfCurrencies
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("symbols")
    suspend fun getSymbols(@Query("apikey") apikey: String): ListOfCurrencies

}