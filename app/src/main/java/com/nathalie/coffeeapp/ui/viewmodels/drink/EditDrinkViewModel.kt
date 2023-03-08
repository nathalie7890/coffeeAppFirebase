package com.nathalie.coffeeapp.ui.viewmodels.drink

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import com.nathalie.coffeeapp.ui.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditDrinkViewModel @Inject constructor(repo: DrinkRepository) : BaseDrinkViewModel(repo) {
    val drink = MutableLiveData<Drink>()

    fun getDrinkById(id: String) {
        viewModelScope.launch {
            val res = safeApiCall { repo.getDrinkById(id) }
            res?.let {
                drink.value = it
            }
        }
    }

    fun editDrink(id: String, drink: Drink, imageUri: Uri) {
        val validationStatus = Utils.validate(
            drink.title,
            drink.subtitle,
            drink.details,
            drink.ingredients,
            drink.category.toString()
        )

        viewModelScope.launch {
            if (validationStatus) {
                safeApiCall { repo.updateDrink(id, drink) }
                finish.emit(Unit)
            } else {
                viewModelScope.launch {
                    error.emit("Kindly provide all information")
                }
            }
        }
    }
}