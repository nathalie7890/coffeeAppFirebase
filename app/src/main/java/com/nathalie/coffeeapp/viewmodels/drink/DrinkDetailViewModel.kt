package com.nathalie.coffeeapp.viewmodels.drink

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.Drink
import com.nathalie.coffeeapp.repository.DrinkRepository
import kotlinx.coroutines.launch

class DrinkDetailViewModel(private val repo: DrinkRepository) : ViewModel() {
    val drink: MutableLiveData<Drink> = MutableLiveData()

    //find drink that matches the id
    fun getDrinkById(id: Long) {
        viewModelScope.launch {
            val res = repo.getDrinkById(id)
            res?.let {
                drink.value = it
            }
        }
    }

    //delete drink that matches the id
    fun deleteDrink(id: Long) {
        viewModelScope.launch {
            repo.deleteDrink(id)
        }
    }

    class Provider(val repo: DrinkRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DrinkDetailViewModel(repo) as T
        }
    }
}