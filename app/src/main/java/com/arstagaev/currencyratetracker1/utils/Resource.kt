package com.arstagaev.currencyratetracker1.utils

sealed class Resource<out R>(
//    val data: out R? = null,
//    val message: String? = null,
//    val code   : Int = 0
) {
    //data class Init<T> : Resource<T>(message = "initialized")
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error<T>(val exception: Exception) : Resource<T>()
    object Loading : Resource<Nothing>()

}

//sealed class DataState<out R> {
//    data class Success<out T>(val data: T) : DataState<T>()
//    data class Error(val exception: Exception) : DataState<Nothing>()
//    object Loading : DataState<Nothing>()
//}