package com.arstagaev.currencyratetracker1.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arstagaev.currencyratetracker1.ui.navigation.Screen
import com.arstagaev.currencyratetracker1.ui.screens.AllCurrenciesScreen
import com.arstagaev.currencyratetracker1.ui.screens.FavoriteCurrenciesScreen
import com.arstagaev.currencyratetracker1.ui.screens.SortScreen
import com.arstagaev.currencyratetracker1.ui.theme.ColorBackground
import com.arstagaev.currencyratetracker1.ui.theme.CurrencyRateTracker1Theme
import com.arstagaev.currencyratetracker1.utils.CurRDrawable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: MainActivityViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        installSplashScreen().apply {
//            setKeepOnScreenCondition { splashViewModel.isLoading.value }
//        }

        mainViewModel.getAvailableCurrencies()

        setContent {
            CurrencyRateTracker1Theme {
                val scaffoldState = rememberScaffoldState()
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ColorBackground),
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopBar()
                    },
                    content = {
                        NavigationScreen(navController, it)
                    },
                    bottomBar = {
                        BottomBar(navController)
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun TopBar() {
        Row(
            Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(Color.LightGray),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            var expanded           by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf("USD") }
            //val modifier = Modifier.fillMaxSize().weight(1f)

            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .align(Alignment.CenterVertically)
                .padding(vertical = 10.dp)) {
                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxSize(),
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        modifier=Modifier.fillMaxWidth(),
                        readOnly = true,
                        value = selectedOptionText,
                        onValueChange = {  },
                        label = { Text("Выбранная валюта:", fontSize = 20.sp, color = Color.Black) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        textStyle = TextStyle.Default.copy(fontSize = 30.sp),
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.Transparent,
                            placeholderColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,   // bottom line in like edittext
                            unfocusedIndicatorColor = Color.Transparent, // bottom line in like edittext
                            cursorColor = Color.Transparent,
                        )//, textStyle = TextStyle(fontSize = TextUnit(20f, TextUnitType.Sp))
                    )

                    ExposedDropdownMenu(
                        modifier=Modifier.fillMaxWidth(),
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        mainViewModel.listOfAvailableCurrencies.forEach { selectionOption ->
                            DropdownMenuItem(
                                modifier = Modifier.fillMaxSize(),
                                onClick = {
                                    selectedOptionText = selectionOption.abbreviation

                                    expanded = false


                                }
                            ) {
                                Text(text = selectionOption.abbreviation)
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(vertical = 20.dp, horizontal = 30.dp)
            ) {
                Image(modifier = Modifier.size(60.dp), painter = painterResource(id = CurRDrawable.baseline_sort_24), contentDescription = "Sort")
                //Text(modifier = Modifier, text = "Сортировка", fontSize = 20.sp, color = Color.Black)
            }
        }
    }
    @Composable
    fun NavigationScreen(
        navController: NavHostController,
        paddingValues: PaddingValues
    ) {
        // create vm by factory
        var mainViewModel = hiltViewModel<MainViewModel>()
        //val availableCurrencies = mainViewModel.bleCommandTrain.collectAsState(initial = null) //remember {  }

        LaunchedEffect(true) {
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
        NavHost(
            navController = navController,
            startDestination = Screen.AllCurrencies.route
        ) {
            composable(
                route = Screen.AllCurrencies.route
            ) {
                AllCurrenciesScreen(navController,mainViewModel)
            }

            composable(
                route = Screen.FavCurrencies.route
            ) {
                FavoriteCurrenciesScreen(navController,mainViewModel)

                //mainViewModel.refreshPairCurrenciesFromDB()
            }

            composable(
                route = Screen.ToSortCurrencies.route
            ) {
                SortScreen(navController,mainViewModel)
                //mainViewModel.refreshPairCurrenciesFromDB()
            }

        }
    }

    @Composable
    fun BottomBar(navController: NavHostController) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(Color.Gray),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        navController.navigate(Screen.AllCurrencies.route)

                    }) {
                Text(modifier = Modifier.align(Alignment.Center), text = "Популярное", color = Color.Black)
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        navController.navigate(Screen.FavCurrencies.route)
                    }) {
                Text(modifier = Modifier.align(Alignment.Center), text = "Избранное", color = Color.Black)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.refreshPairCurrenciesFromDB()
    }
}
