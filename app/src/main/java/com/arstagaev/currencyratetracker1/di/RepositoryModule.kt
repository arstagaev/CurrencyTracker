package com.arstagaev.currencyratetracker1.di

import com.arstagaev.currencyratetracker1.data.CurrencyRepository
import com.arstagaev.currencyratetracker1.data.local.db.CurrencyDao
import com.arstagaev.currencyratetracker1.data.remote.CurrencyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /**
     * Provides RemoteDataRepository for access api service method
     */
    @Singleton
    @Provides
    fun provideCurrencyRepository(
        currencyApi: CurrencyApi,
        currencyDao: CurrencyDao
    ): CurrencyRepository {
        return CurrencyRepository(
            currencyApi,currencyDao
        )
    }

}