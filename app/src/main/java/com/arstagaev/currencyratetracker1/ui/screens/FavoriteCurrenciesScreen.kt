package com.arstagaev.currencyratetracker1.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.arstagaev.currencyratetracker1.data.local.db.models.CurrencyDto
import com.arstagaev.currencyratetracker1.ui.MainViewModel
import com.arstagaev.currencyratetracker1.ui.custom_tools.Pendulum
import com.arstagaev.currencyratetracker1.ui.custom_tools.ShimmerAnimation
import com.arstagaev.currencyratetracker1.ui.theme.ColorBackground
import com.arstagaev.currencyratetracker1.utils.CurRDrawable
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavoriteCurrenciesScreen(navController: NavHostController, mainViewModel: MainViewModel) {

    Column(
        Modifier
            .fillMaxSize()
            .background(ColorBackground)
    ) {

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(
                mainViewModel.listOfPairCurrencies//.filter { it.isFavorite }
            ) { index: Int, item: CurrencyDto ->

                if(mainViewModel.isLoading.value) {
                    repeat(3) {
                        ShimmerAnimation()
                    }

                }else {
                    AnimatedVisibility(
                        visible = item.isFavorite,
                        //enter = scaleIn(animationSpec = tween(durationMillis = 1000)),
                        exit = scaleOut(animationSpec = tween(durationMillis = 1000))
                    ) {
                        if (mainViewModel.isPendulumState.value) {
                            Pendulum() {
                                CurrencyFavRow(index,item,mainViewModel)
                            }
                        } else {
                            CurrencyFavRow(index,item,mainViewModel)
                        }

                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CurrencyFavRow(index: Int, item: CurrencyDto, mainViewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var isFav by remember { mutableStateOf(item.isFavorite) }
    // This will detect any changes to data and recompose your composable.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(7.dp),
        elevation = 20.dp
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(0.9f))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { },
                        onDoubleTap = { },
                        onLongPress = {
                            // My easter egg for reviewer:
                            mainViewModel.isPendulumState.value =
                                !mainViewModel.isPendulumState.value
                        },
                        onTap = { }
                    )
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = "${item.abbreviation}  ${item.value}",
                color = Color.Black,
                fontSize = 20.sp
            )
            Box(
                Modifier
                    .size(50.dp)
                    .padding(16.dp)
                    .clickable {

                        coroutineScope.launch {
                            mainViewModel.updateFavState(
                                index,
                                item.abbreviation,
                                newFavoriteState = !item.isFavorite
                            )
                        }
                        //isFav = !item.isFavorite
                        mainViewModel.listOfPairCurrencies[index] =
                            mainViewModel.listOfPairCurrencies[index].copy(isFavorite = !item.isFavorite)
                        //mainViewModel.updateUI()
//                        mainViewModel.changeValue(
//                            index,
//                            CurrencyDto(
//                                item.abbreviation,
//                                item.name,
//                                item.isBase,
//                                !item.isFavorite,
//                                item.value
//                            )
//                        )

                    }
            ) {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(
                        id = if (mainViewModel.listOfPairCurrencies[index].isFavorite) CurRDrawable.baseline_star_24 else CurRDrawable.baseline_star_border_24
                    ),
                    contentDescription = "favorite"
                )
            }


        }
    }

}