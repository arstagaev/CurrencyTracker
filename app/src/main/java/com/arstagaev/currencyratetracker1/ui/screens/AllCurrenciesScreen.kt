package com.arstagaev.currencyratetracker1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.arstagaev.currencyratetracker1.ui.MainViewModel
import com.arstagaev.currencyratetracker1.data.local.db.models.CurrencyDto
import com.arstagaev.currencyratetracker1.ui.custom_tools.ShimmerAnimation
import com.arstagaev.currencyratetracker1.ui.theme.ColorBackground
import com.arstagaev.currencyratetracker1.utils.CurRDrawable
import kotlinx.coroutines.launch


@Composable
fun AllCurrenciesScreen(navController: NavHostController, mainViewModel: MainViewModel) {


    Column(
        Modifier.fillMaxSize( ).background(ColorBackground)
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(mainViewModel.listOfPairCurrencies) { index: Int, item: CurrencyDto ->

                if(mainViewModel.isLoading.value) {
                    repeat(3) {
                        ShimmerAnimation()
                    }

                } else {
                    CurrencyRow(index,item,mainViewModel)
                }

            }
        }
    }
}
@Composable
fun CurrencyRow(index: Int, item: CurrencyDto, mainViewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var isFav by remember { mutableStateOf(item.isFavorite) }

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
                .background(Color.White),
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
                            mainViewModel.updateFavState(index, item.abbreviation, newFavoriteState = !item.isFavorite)
                        }
                        isFav = !item.isFavorite
                        mainViewModel.updateUI()

                    }
            ) {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(
                        id = if (isFav) CurRDrawable.baseline_star_24 else CurRDrawable.baseline_star_border_24
                    ),
                    contentDescription = "favorite"
                )
            }


        }
    }
}