package com.nathalie.coffeeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.Model.Drink
import com.nathalie.coffeeapp.repository.DrinkRepository
import kotlinx.coroutines.launch

class AddDrinkViewModel(private val repo: DrinkRepository): ViewModel() {
    fun addDrink(drink: Drink) {
        viewModelScope.launch {
            repo.addDrink(drink)
        }
    }

    class Provider(private val repo: DrinkRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddDrinkViewModel(repo) as T
        }
    }
}