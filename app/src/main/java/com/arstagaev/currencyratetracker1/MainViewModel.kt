package com.arstagaev.currencyratetracker1

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arstagaev.currencyratetracker1.data.CurrencyRepository
import com.arstagaev.currencyratetracker1.data.remote.models.ListOfCurrencies
import com.arstagaev.currencyratetracker1.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: CurrencyRepository) : ViewModel() {

    val availableCurrencies: MutableState<Resource<ListOfCurrencies>?> = mutableStateOf(null)

    private val _aval: MutableState<Resource<ListOfCurrencies>?> = mutableStateOf(null)
    val available: State<Resource<ListOfCurrencies>?> get() = _aval

    //private val _currentDoorLocks = MutableStateFlow<Resource<ListOfCurrencies>>()
    //val currentDoorLocks : StateFlow<ListOfCurrencies> get() =  _currentDoorLocks //get() = _currencyPrices
//    val recommendedMovie: MutableState<DataState<BaseModel>?> = mutableStateOf(null)
//    val artist: MutableState<DataState<Artist>?> = mutableStateOf(null)
    var bleCommandTrain = MutableSharedFlow<Resource<ListOfCurrencies>>(50,50, BufferOverflow.SUSPEND)

    fun getAvailableCurrencies() {
        viewModelScope.launch {
            repo.getAvailableCurrencies().collect {
                bleCommandTrain.emit(it)
                _aval.value = it
                //_currentDoorLocks.value = (it)
            }//.launchIn(viewModelScope)
        }
    }
}