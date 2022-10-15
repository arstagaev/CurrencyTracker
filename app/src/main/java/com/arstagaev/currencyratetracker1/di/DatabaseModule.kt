package com.arstagaev.currencyratetracker1.di

import android.app.Application
import androidx.room.Room
import com.arstagaev.currencyratetracker1.data.local.db.AppDatabase
import com.arstagaev.currencyratetracker1.data.local.db.CurrencyDao
import com.arstagaev.currencyratetracker1.utils.CurRString
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Provides
  @Singleton
  fun provideAppDatabase(application: Application): AppDatabase {
    return Room
      .databaseBuilder(
        application,
        AppDatabase::class.java,
        application.getString(CurRString.db_name)
      )
      .fallbackToDestructiveMigration()
      .build()
  }

  @Provides
  @Singleton
  fun provideCurrenciesDao(appDatabase: AppDatabase): CurrencyDao {
    return appDatabase.currencyDao()
  }
}
