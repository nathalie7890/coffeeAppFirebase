package com.nathalie.coffeeapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.Model.Drink
import com.nathalie.coffeeapp.repository.DrinkRepository
import kotlinx.coroutines.launch

class DrinkViewModel(val repo: DrinkRepository) : ViewModel() {
    val drinks: MutableLiveData<List<Drink>> = MutableLiveData()

    init {
        getDrinks("")
    }

    fun getDrinks(str: String) {
        viewModelScope.launch {
            val res = repo.getDrinks(str)
            drinks.value = res
        }
    }

    class Provider(val repo: DrinkRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DrinkViewModel(repo) as T
        }
    }
}