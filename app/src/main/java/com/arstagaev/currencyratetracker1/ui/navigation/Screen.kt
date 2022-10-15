package com.arstagaev.currencyratetracker1.ui.navigation

sealed class Screen(val route: String) {
    object ListOfAvailableCur: Screen("coin_list_screen")
    //object CoinDetailScreen: Screen("coin_detail_screen")
}
