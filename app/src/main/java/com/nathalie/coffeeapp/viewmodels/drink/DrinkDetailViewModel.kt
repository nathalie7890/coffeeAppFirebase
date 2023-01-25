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
            repo.getDrinkById(id).collect() {
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

    fun favDrink(id: Long, status: Boolean) {
        viewModelScope.launch {
            repo.favDrink(id, status)
        }
    }

    fun isFav(): Boolean {
        return drink.value?.favorite == true
    }

    class Provider(val repo: DrinkRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DrinkDetailViewModel(repo) as T
        }
    }
}