package com.nathalie.coffeeapp.viewmodels.drink

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.Drink
import com.nathalie.coffeeapp.repository.DrinkRepository
import kotlinx.coroutines.launch

class EditDrinkViewModel(private val repo: DrinkRepository): ViewModel() {
    val drink: MutableLiveData<Drink> = MutableLiveData()

    //find Drink that matches the id
    fun getDrinkById(id: Long) {
        viewModelScope.launch {
            val res = repo.getDrinkById(id)
            res?.let {
                drink.value = it
            }
        }
    }

    //edit Drink that matches the id
    fun editDrink(id: Long, drink: Drink) {
        viewModelScope.launch {
            repo.updateDrink(id, drink)
        }
    }
    class Provider(val repo: DrinkRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditDrinkViewModel(repo) as T
        }
    }
}