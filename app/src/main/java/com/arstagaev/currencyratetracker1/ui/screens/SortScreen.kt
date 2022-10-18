package com.arstagaev.currencyratetracker1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.arstagaev.currencyratetracker1.data.local.sharedpref.PreferenceStorage
import com.arstagaev.currencyratetracker1.ui.MainViewModel
import com.arstagaev.currencyratetracker1.ui.enums.SortState
import com.arstagaev.currencyratetracker1.ui.navigation.Screen
import com.arstagaev.currencyratetracker1.ui.theme.ColorBackground
import kotlinx.coroutines.launch

@Composable
fun SortScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val fontSizeRow = 20.sp
    Column(
        Modifier.fillMaxSize().background(ColorBackground).padding()
    ) {
        Box(
            Modifier
                .fillMaxWidth().height(90.dp)
                .background(Color.White)
                //.clickable {  }
        ) {
            Text(
                modifier = Modifier.fillMaxWidth().height(120.dp).padding(start = 10.dp),
                text = "Сортировка:",
                color = Color.Black,
                fontSize = 40.sp
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(10.dp),
            elevation = 5.dp
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .clickable {
                        coroutineScope.launch {
                            mainViewModel.refreshPairCurrenciesFromDB(SortState.BY_ABBREVIATION_ASC)
                        }
                        mainViewModel.sortStyle.value = SortState.BY_ABBREVIATION_ASC
                        navController.navigate(Screen.AllCurrencies.route)
                        PreferenceStorage.sortStyle = SortState.BY_ABBREVIATION_ASC.NCommand
                    }
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().height(120.dp).padding(start = 10.dp),
                    text = "по Алфавиту и Возрастанию",
                    color = Color.Black,
                    fontSize = fontSizeRow
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(10.dp),
            elevation = 5.dp
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .clickable {
                        coroutineScope.launch {
                            mainViewModel.refreshPairCurrenciesFromDB(SortState.BY_ABBREVIATION_DESC)
                        }
                        mainViewModel.sortStyle.value = SortState.BY_ABBREVIATION_DESC
                        navController.navigate(Screen.AllCurrencies.route)
                        PreferenceStorage.sortStyle = SortState.BY_ABBREVIATION_DESC.NCommand
                    }
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().height(120.dp).padding(start = 10.dp),
                    text = "по Алфавиту и Убыванию",
                    color = Color.Black,
                    fontSize = fontSizeRow
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(10.dp),
            elevation = 5.dp
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .clickable {
                        coroutineScope.launch {
                            mainViewModel.refreshPairCurrenciesFromDB(SortState.BY_VALUE_ASC)
                        }
                        mainViewModel.sortStyle.value = SortState.BY_VALUE_ASC
                        navController.navigate(Screen.AllCurrencies.route)
                        PreferenceStorage.sortStyle = SortState.BY_VALUE_ASC.NCommand
                    }
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().height(120.dp).padding(start = 10.dp),
                    text = "по Значению и Возрастанию",
                    color = Color.Black,
                    fontSize = fontSizeRow
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(10.dp),
            elevation = 5.dp
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .clickable {
                        coroutineScope.launch {
                            mainViewModel.refreshPairCurrenciesFromDB(SortState.BY_VALUE_DESC)
                        }
                        mainViewModel.sortStyle.value = SortState.BY_VALUE_DESC
                        navController.navigate(Screen.AllCurrencies.route)
                        PreferenceStorage.sortStyle = SortState.BY_VALUE_ASC.NCommand
                    }
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().height(120.dp).padding(start = 10.dp),
                    text = "по Значению и Убыванию",
                    color = Color.Black,
                    fontSize = fontSizeRow
                )
            }
        }
    }
}