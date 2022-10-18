package com.arstagaev.currencyratetracker1.data.local.sharedpref

import android.content.Context
import android.content.SharedPreferences


object PreferenceStorage {

    private var NAME = "CurrencyRateTracker"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    fun clearAll() {
        preferences.edit {
            it.clear()
        }
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var sortStyle: String
        get() = preferences.getString("nsort","1").toString()
        set(value) = preferences.edit {
            it.putString("nsort", value)
        }

    var baseCurrency: String
        get() = preferences.getString("baseCurrency","USD").toString()
        set(value) = preferences.edit {
            it.putString("baseCurrency", value)
        }

    var lastTimeOfRequestAvailableCurrency: String
        get() = preferences.getString("lastTimeAvailableCurr_request", System.currentTimeMillis().toString() ).toString()
        set(value) = preferences.edit {
            it.putString("lastTimeAvailableCurr_request", value)
        }

}