package com.arstagaev.currencyratetracker1.ui.navigation

sealed class Screen(val route: String) {
    object AllCurrencies: Screen("all")
    object FavCurrencies: Screen("fav")
    object ToSortCurrencies: Screen("sort")
}
