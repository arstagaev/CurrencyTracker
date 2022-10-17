package com.arstagaev.currencyratetracker1

import android.app.Application
import com.arstagaev.currencyratetracker1.data.local.sharedpref.PreferenceStorage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CurrencyRateApp: Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceStorage.init(this)
    }
}
