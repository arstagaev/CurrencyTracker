package com.arstagaev.currencyratetracker1.utils.extensions

import android.content.Context
import android.widget.Toast
import com.arstagaev.currencyratetracker1.utils.logError

fun Context.toast(msg: String) =
    try { Toast.makeText(this,msg ?: "", Toast.LENGTH_SHORT).show() }
    catch (e: Exception) { logError("error in Toast") }
