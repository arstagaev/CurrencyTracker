package com.arstagaev.currencyratetracker1.utils.check_internet

// https://medium.com/scalereal/observing-live-connectivity-status-in-jetpack-compose-way-f849ce8431c7

sealed class ConnectionState{
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}