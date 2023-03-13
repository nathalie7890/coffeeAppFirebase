package com.nathalie.coffeeapp.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val refreshDrinks: MutableLiveData<Pair<Boolean, Boolean>> = MutableLiveData(Pair(false, false))
    val refreshBeans: MutableLiveData<Boolean> = MutableLiveData(false)
    val refreshRoast: MutableLiveData<Boolean> = MutableLiveData(false)
    var fav = false


    fun shouldRefreshDrinks(refresh: Boolean, fav: Boolean = false) {
        refreshDrinks.value = Pair(refresh, fav)
    }

    fun shouldRefreshBeans(refresh: Boolean) {
        refreshBeans.value = refresh
    }

    fun shouldRefreshRoast(refresh: Boolean) {
        refreshRoast.value = refresh
    }
}