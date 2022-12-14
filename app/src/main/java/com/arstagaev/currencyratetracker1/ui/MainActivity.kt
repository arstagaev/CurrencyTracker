package com.arstagaev.currencyratetracker1.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arstagaev.currencyratetracker1.data.local.db.models.AvailableCurrencyDto
import com.arstagaev.currencyratetracker1.data.local.sharedpref.PreferenceStorage
import com.arstagaev.currencyratetracker1.ui.navigation.Screen
import com.arstagaev.currencyratetracker1.ui.screens.AllCurrenciesScreen
import com.arstagaev.currencyratetracker1.ui.screens.FavoriteCurrenciesScreen
import com.arstagaev.currencyratetracker1.ui.screens.SortScreen
import com.arstagaev.currencyratetracker1.ui.theme.ColorBackground
import com.arstagaev.currencyratetracker1.ui.theme.CurrencyRateTracker1Theme
import com.arstagaev.currencyratetracker1.utils.CurRDrawable
import com.arstagaev.currencyratetracker1.utils.check_internet.ConnectionState
import com.arstagaev.currencyratetracker1.utils.check_internet.connectivityState
import com.arstagaev.currencyratetracker1.utils.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(lifecycleScope.coroutineContext).launch {
            mainViewModel.toastReminder.collect {
                applicationContext.toast("${it}")
            }
        }

        setContent {
            CurrencyRateTracker1Theme {
                val scaffoldState = rememberScaffoldState()
                val navController = rememberNavController()
                val connection by connectivityState()
                mainViewModel.connectionState.value = connection

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ColorBackground),
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopBar(navController)
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

    @Composable
    fun TopBar(navController: NavHostController) {

        Column(Modifier.fillMaxWidth())
        {
            AnimatedVisibility(visible = mainViewModel.connectionState.value == ConnectionState.Unavailable){
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(Color.DarkGray),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(modifier = Modifier, text = "?????? ????????, ?????????????????? ?????????????????? ??????????????????????", color = Color.Red, textAlign = TextAlign.Center)

                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.Blue.copy(0.1f)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {

                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(2f)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            mainViewModel.isShowingDialog.value = true
                        }) {
                    Text(modifier = Modifier.align(Alignment.Center), text = buildAnnotatedString {
                        append("?????????????????? ????????????: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold))
                        {
                            append("${mainViewModel.baseCurrency.value}")
                        }
                    }, color = Color.Black)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .padding(vertical = 20.dp, horizontal = 30.dp)
                        .clickable {
                            navController.navigate(Screen.ToSortCurrencies.route)


                        }
                ) {
                    Image(modifier = Modifier.size(60.dp), painter = painterResource(id = CurRDrawable.baseline_sort_24), contentDescription = "Sort")
                }
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


        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = Screen.AllCurrencies.route
            ) {
                composable(
                    route = Screen.AllCurrencies.route
                ) {
                    AllCurrenciesScreen(mainViewModel)
                }

                composable(
                    route = Screen.FavCurrencies.route
                ) {
                    FavoriteCurrenciesScreen(mainViewModel)
                }

                composable(
                    route = Screen.ToSortCurrencies.route
                ) {
                    SortScreen(navController,mainViewModel)
                }
            }
            if (mainViewModel.isShowingDialog.value) {
                listOfCurrencies()
            }

        }

    }

    @Composable
    fun BottomBar(navController: NavHostController) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(Color.Blue.copy(0.5f)),
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
                Text(modifier = Modifier.align(Alignment.Center), text = "????????????????????", color = Color.Black)
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        navController.navigate(Screen.FavCurrencies.route)
                    }) {
                Text(modifier = Modifier.align(Alignment.Center), text = "??????????????????", color = Color.Black)
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun listOfCurrencies() {
        var ctx = LocalContext.current

        Dialog(
            onDismissRequest = {
                mainViewModel.isShowingDialog.value = false
            },
            DialogProperties(usePlatformDefaultWidth = true, dismissOnBackPress = true, dismissOnClickOutside = true),
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "???????????????? ????????????:",
                        color = Color.Black,
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .padding(10.dp), textAlign = TextAlign.Center,
                        fontSize = 20.sp,fontFamily = FontFamily.Default
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        LazyColumn(modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)) {
                            itemsIndexed(mainViewModel.listOfAvailableCurrencies) { index: Int, item: AvailableCurrencyDto ->
                                Text(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(bottom = 10.dp)
                                        .clickable {
                                            if (mainViewModel.connectionState.value == ConnectionState.Available) {
                                                mainViewModel.let {
                                                    it.baseCurrency.value = item.abbreviation
                                                    it.refreshCurrencyPairs(baseCurrency = item.abbreviation)
                                                    it.isShowingDialog.value = false
                                                }

                                                PreferenceStorage.baseCurrency = item.abbreviation
                                            } else {
                                                ctx.toast("?????? ????????. ?????????????? ??????????")
                                            }

                                        },
                                    text = "${item.abbreviation} (${item.name})", fontSize = 20.sp, color = Color.Black)
                            }
                        }
                    }


                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.refreshPairCurrenciesFromDB(mainViewModel.sortStyle.value)
    }
}
