package com.nathalie.coffeeapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val refreshDrinks: MutableLiveData<Boolean> = MutableLiveData(false)
    val refreshBeans: MutableLiveData<Boolean> = MutableLiveData(false)
    val refreshRoast: MutableLiveData<Boolean> = MutableLiveData(false)

    fun shouldRefreshDrinks(refresh: Boolean) {
        refreshDrinks.value = refresh
    }

    fun shouldRefreshBeans(refresh: Boolean) {
        refreshBeans.value = refresh
    }

    fun shouldRefreshRoast(refresh: Boolean) {
        refreshRoast.value = refresh
    }
}