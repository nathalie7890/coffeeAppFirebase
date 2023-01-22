package com.nathalie.coffeeapp.viewmodels.drink

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.Drink
import com.nathalie.coffeeapp.repository.DrinkRepository
import kotlinx.coroutines.launch

class DrinkViewModel(private val repo: DrinkRepository) : ViewModel() {
    val drinks: MutableLiveData<List<Drink>> = MutableLiveData()

    init {
        getDrinks("", 0)
    }

    fun getDrinks(str: String, cat:Int) {
        viewModelScope.launch {
            var res = repo.getDrinks(str, cat)
            res = res.reversed()
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