package com.nathalie.coffeeapp.ui.viewmodels.drink

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDrinkViewModel @Inject constructor(repo: DrinkRepository) : BaseDrinkViewModel(repo) {
    fun addDrink(
        drink: Drink
    ) {
        val validationStatus = validate(
            drink.title,
            drink.subtitle,
            drink.details,
            drink.ingredients,
            drink.category.toString(),
        )

        viewModelScope.launch {
            if (validationStatus) {
                safeApiCall { repo.addDrink(drink) }
                finish.emit(Unit)
            } else {
                error.emit("Kindly provide all information")
            }
        }
    }
}