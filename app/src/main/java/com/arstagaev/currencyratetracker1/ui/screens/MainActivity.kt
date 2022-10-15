package com.arstagaev.currencyratetracker1.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arstagaev.currencyratetracker1.MainViewModel
import com.arstagaev.currencyratetracker1.ui.navigation.Screen
import com.arstagaev.currencyratetracker1.ui.theme.CurrencyRateTracker1Theme
import com.arstagaev.currencyratetracker1.utils.logAction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //private val splashViewModel: MainActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        installSplashScreen().apply {
//            setKeepOnScreenCondition { splashViewModel.isLoading.value }
//        }

        setContent {
            CurrencyRateTracker1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.ListOfAvailableCur.route
                    ) {
                        composable(
                            route = Screen.ListOfAvailableCur.route
                        ) {
                            MainScreen()
                        }

                    }

                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        var mainViewModel = hiltViewModel<MainViewModel>()
        val availableCurrencies = mainViewModel.bleCommandTrain.collectAsState(initial = null) //remember {  }

        LaunchedEffect(true) {
            mainViewModel.getAvailableCurrencies()
//            availableCurrencies.collect {
//                logAction("ANSWER::: ${it.toString()}")
//            }
//            availableCurrencies.value?.let {
//                logAction("ANSWER::: ${it.value}")
//            }
        }
//        LazyColumn(modifier = listModifier) {
//            itemsIndexed(availableCurrencies) {
//
//            }
//        }
    }
}

