package com.arstagaev.currencyratetracker1.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arstagaev.currencyratetracker1.data.CurrencyRepository
import com.arstagaev.currencyratetracker1.data.local.db.models.CachedCurrencyPairsDto
import com.arstagaev.currencyratetracker1.data.local.db.models.AvailableCurrencyDto
import com.arstagaev.currencyratetracker1.data.local.db.models.CurrencyDto
import com.arstagaev.currencyratetracker1.data.remote.models.AvailableCurrencies
import com.arstagaev.currencyratetracker1.utils.Resource
import com.arstagaev.currencyratetracker1.utils.logAction
import com.arstagaev.currencyratetracker1.utils.logError
import com.arstagaev.currencyratetracker1.utils.logInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: CurrencyRepository) : ViewModel() {

    val availableCurrencies: MutableState<Resource<AvailableCurrencies>?> = mutableStateOf(null)

    private val _aval: MutableState<Resource<AvailableCurrencies>?> = mutableStateOf(null)
    val available: State<Resource<AvailableCurrencies>?> get() = _aval

    // Using mutableStateListOf to hold the list of items, this will recompose when the list changes.


    var listOfAvailableCurrencies = mutableStateListOf<AvailableCurrencyDto>()



    var listOfPairCurrencies = mutableStateListOf<CurrencyDto>()

    //private val _elements2 = mutableStateListOf<CurrencyDto>()
    //val elements2: List<CurrencyDto> = listOfPairCurrencies

    var isLoading = mutableStateOf(true)
    var isPendulumState = mutableStateOf(false)
    //private val _currentDoorLocks = MutableStateFlow<Resource<ListOfCurrencies>>()
    //val currentDoorLocks : StateFlow<ListOfCurrencies> get() =  _currentDoorLocks //get() = _currencyPrices
//    val recommendedMovie: MutableState<DataState<BaseModel>?> = mutableStateOf(null)
//    val artist: MutableState<DataState<Artist>?> = mutableStateOf(null)
    var bleCommandTrain = MutableSharedFlow<Resource<AvailableCurrencies>>(50,50, BufferOverflow.SUSPEND)

    init {
        isLoading.value = true
        refreshCurrencyPairs("USD")
    }
    var onUpdate = mutableStateOf(0)

    fun updateUI() {
        onUpdate.value = (0..1_000_000).random()
    }
    fun getAvailableCurrencies() {
        listOfAvailableCurrencies.clear()

        viewModelScope.launch(Dispatchers.IO) {
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

                        logInfo("input: ${inputAvailableCur.joinToString()}")

                        // get may be new
                        refreshAvailableCurrenciesFromDB()
                        isLoading.value = false
                    }
                    is Resource.Error -> {

                        logError("Error in: getAvailableCurrencies()")
                        // may be we are offline:
                        refreshAvailableCurrenciesFromDB()
                        isLoading.value = false
                    }
                }
//                bleCommandTrain.emit(it)
//                _aval.value = it
                //_currentDoorLocks.value = (it)
            }.launchIn(viewModelScope)
        }
    }

    fun refreshCurrencyPairs(baseCurrency: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllPairsCurrencies(baseCurrency = baseCurrency).onEach {
                when(it) {
                    is Resource.Loading -> {
                        logInfo("Loading..")
                        isLoading.value = true
                    }
                    is Resource.Success -> {
                        logInfo("Success..")

                        //for tests
                        val inputAvailableCur = arrayListOf<CachedCurrencyPairsDto>()


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
                        refreshPairCurrenciesFromDB()
                        isLoading.value = false
                    }
                    is Resource.Error -> {
                        logError("Error in: getAvailableCurrencies()")
                        // may be we are offline:
                        refreshPairCurrenciesFromDB()
                        isLoading.value = false
                    }
                }
//                bleCommandTrain.emit(it)
//                _aval.value = it
                //_currentDoorLocks.value = (it)
            }.launchIn(viewModelScope)
        }
    }

    suspend fun updateFavState(index: Int, abbreviation: String, newFavoriteState: Boolean) {
        listOfPairCurrencies[index] = listOfPairCurrencies[index].also { it.isFavorite = newFavoriteState}
        repo.updateFavCurrency(abbreviation = abbreviation, isFavorite = newFavoriteState)
        //getPairCurrenciesFromDB()
    }



    // get

    private fun refreshAvailableCurrenciesFromDB() {
        viewModelScope.launch {
            isLoading.value = true
            listOfAvailableCurrencies.clear()

            logAction(">>>  ${repo.getCachedCurrencies()?.joinToString()}")

            repo.getCachedCurrencies()?.let { listOfAvailableCurrencies.addAll(it) }
            isLoading.value = false
        }

    }

    fun refreshPairCurrenciesFromDB() {
        viewModelScope.launch {
            isLoading.value = true
            listOfPairCurrencies.clear()

            logAction(">>>  ${repo.getWholeListCurrencies()?.size}")
            delay(10)
            repo.getWholeListCurrencies()?.let { listOfPairCurrencies.addAll(it) }
            delay(100)
            repo.getWholeListCurrencies()?.forEach {
                println("~~~ ${it.toString()}")
            }
            logAction("<>>> ${listOfPairCurrencies.joinToString()}")
            isLoading.value = false
        }

    }
}