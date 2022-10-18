package com.arstagaev.currencyratetracker1.ui

import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arstagaev.currencyratetracker1.data.CurrencyRepository
import com.arstagaev.currencyratetracker1.data.local.db.models.CachedCurrencyPairsDto
import com.arstagaev.currencyratetracker1.data.local.db.models.AvailableCurrencyDto
import com.arstagaev.currencyratetracker1.data.local.db.models.CurrencyDto
import com.arstagaev.currencyratetracker1.data.local.sharedpref.PreferenceStorage
import com.arstagaev.currencyratetracker1.data.remote.models.AvailableCurrencies
import com.arstagaev.currencyratetracker1.ui.enums.SortState
import com.arstagaev.currencyratetracker1.utils.Resource
import com.arstagaev.currencyratetracker1.utils.check_internet.ConnectionState
import com.arstagaev.currencyratetracker1.utils.logAction
import com.arstagaev.currencyratetracker1.utils.logError
import com.arstagaev.currencyratetracker1.utils.logInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: CurrencyRepository) : ViewModel() {

    var listOfAvailableCurrencies = mutableStateListOf<AvailableCurrencyDto>()
    var listOfPairCurrencies = mutableStateListOf<CurrencyDto>()

    var baseCurrency = mutableStateOf(PreferenceStorage.baseCurrency)

    var sortStyle = mutableStateOf(
            when(PreferenceStorage.sortStyle) {
                "1" -> SortState.BY_ABBREVIATION_ASC
                "2" -> SortState.BY_ABBREVIATION_DESC
                "3" -> SortState.BY_VALUE_ASC
                "4" -> SortState.BY_VALUE_DESC
                else -> SortState.BY_ABBREVIATION_ASC
            }
        )
    var isLoading = mutableStateOf(true)
    var isPendulumState = mutableStateOf(false)
    var isShowingDialog = mutableStateOf(false)
    var connectionState = mutableStateOf<ConnectionState>(ConnectionState.Unavailable)

    init {
        isLoading.value = true
        refreshCurrencyPairs(baseCurrency.value)
    }

    fun getAvailableCurrencies(): Boolean {
        var isSuccess = false
        listOfAvailableCurrencies.clear()

        viewModelScope.launch(Dispatchers.IO) {
            if (isNotFrequentRequest() && connectionState.value == ConnectionState.Available) {
                repo.getAvailableCurrencies().onEach {
                    when(it) {
                        is Resource.Loading -> {
                            logInfo("Loading..")
                            isLoading.value = true
                        }
                        is Resource.Success -> {
                            logInfo("Success..")
                            //for tests
                            val inputAvailableCur = arrayListOf<AvailableCurrencyDto>()
                            //getAvailableCurrenciesFromDB()


                            it.data.symbols?.forEach { (abbreviation, name) ->
                                inputAvailableCur.add(AvailableCurrencyDto( abbreviation = abbreviation, name = name))

                                repo.setAvailableCurrency(
                                    AvailableCurrencyDto(
                                        abbreviation = abbreviation,
                                        name = name,
                                        isBase = false,
                                        isFavorite = false
                                    )
                                )

                            }

                            // get may be new
                            refreshAvailableCurrenciesFromDB()
                            isLoading.value = false
                            isSuccess = true
                        }
                        is Resource.Error -> {

                            logError("Error in: getAvailableCurrencies()")
                            // may be we are offline:
                            refreshAvailableCurrenciesFromDB()
                            isLoading.value = false
                            isSuccess = false
                        }
                    }
                }.launchIn(viewModelScope)
            } else {
                refreshAvailableCurrenciesFromDB()
                isLoading.value = false
                isSuccess = false
            }

        }
        return isSuccess
    }

    fun refreshCurrencyPairs(baseCurrency: String?): Boolean {
        var isSuccess = false
        if(baseCurrency == null || baseCurrency.toCharArray().size != 3) {
            return isSuccess
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (connectionState.value == ConnectionState.Available) {
                repo.getAllPairsCurrencies(baseCurrency = baseCurrency).onEach {
                    when(it) {
                        is Resource.Loading -> {
                            logInfo("Loading..")
                            isLoading.value = true
                        }
                        is Resource.Success -> {
                            logInfo("Success..")

                            var inputAvailableCur = arrayListOf<CachedCurrencyPairsDto>()


                            it.data.rates?.forEach { (abbreviation, value)->
                                inputAvailableCur.add(
                                    CachedCurrencyPairsDto(
                                        abbreviation = abbreviation,
                                        value = (value.toDoubleOrNull() ?: "0") as Double
                                    )
                                )
                            }

                            repo.saveCurrenciesPairs(inputAvailableCur)


                            logInfo("input: ${inputAvailableCur.joinToString()}")

                            // get may be new
                            refreshPairCurrenciesFromDB(sortStyle.value)
                            isLoading.value = false
                            isSuccess = true
                        }
                        is Resource.Error -> {
                            logError("Error in: getAvailableCurrencies()")
                            // may be we are offline:
                            refreshPairCurrenciesFromDB(sortStyle.value)
                            isLoading.value = false
                            isSuccess = false

                        }
                    }
                }.launchIn(viewModelScope)
            } else {
                refreshPairCurrenciesFromDB(sortStyle.value)
                isLoading.value = false
                isSuccess = false
            }

        }
        return isSuccess
    }

    suspend fun updateFavState(index: Int, abbreviation: String, newFavoriteState: Boolean) {
        listOfPairCurrencies[index] = listOfPairCurrencies[index].also { it.isFavorite = newFavoriteState}
        repo.updateFavCurrency(abbreviation = abbreviation, isFavorite = newFavoriteState)
    }

    suspend fun sortByAbbreviation(byAscending: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.sortByAbbreviation(byAscending)
            refreshPairCurrenciesFromDB(sortStyle.value)
        }

    }


    /**
     * Refresh UI from DB
     */

    private fun refreshAvailableCurrenciesFromDB() {
        viewModelScope.launch {
            isLoading.value = true
            listOfAvailableCurrencies.clear()

            repo.getCachedCurrencies()?.let { listOfAvailableCurrencies.addAll(it) }
            delay(100)
            isLoading.value = false
        }

    }

    fun refreshPairCurrenciesFromDB(sortState: SortState) {
        viewModelScope.launch {
            isLoading.value = true
            listOfPairCurrencies.clear()

            repo.getWholeListCurrencies(sortState)?.let { listOfPairCurrencies.addAll(it) }
            delay(100)

            logAction("refreshPairCurrenciesFromDB(): ${listOfPairCurrencies.joinToString()}")
            isLoading.value = false
        }

    }

    private fun isNotFrequentRequest() : Boolean {
        val lastRequestTime = PreferenceStorage.lastTimeOfRequestAvailableCurrency.toLongOrNull()

        return lastRequestTime != null && (System.currentTimeMillis() - lastRequestTime) < AVAILABLE_REQUEST_INTERVAL
    }

    companion object {
        // 2 hours:
        const val AVAILABLE_REQUEST_INTERVAL = 7_200_000L
    }
}