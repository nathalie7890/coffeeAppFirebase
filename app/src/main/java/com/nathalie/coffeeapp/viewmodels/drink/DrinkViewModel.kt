package com.nathalie.coffeeapp.viewmodels.drink

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.Drink
import com.nathalie.coffeeapp.repository.DrinkRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DrinkViewModel(private val repo: DrinkRepository) : ViewModel() {
    val drinks: MutableLiveData<List<Drink>> = MutableLiveData()
    val swipeRefreshLayoutFinished: MutableSharedFlow<Unit> = MutableSharedFlow()

    init {
        getDrinks("", 0, false)
    }

    fun getDrinks(str: String, cat: Int, fav: Boolean) {
        viewModelScope.launch {
            var res = repo.getDrinks(str, cat, fav)
            res = res.reversed()
            drinks.value = res
        }
    }


    fun onRefresh() {
        viewModelScope.launch {
            delay(1000)
            getDrinks("", 0, false)
            swipeRefreshLayoutFinished.emit(Unit)
        }
    }

    class Provider(val repo: DrinkRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DrinkViewModel(repo) as T
        }
    }
}