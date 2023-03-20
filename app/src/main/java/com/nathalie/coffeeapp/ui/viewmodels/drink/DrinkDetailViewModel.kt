package com.nathalie.coffeeapp.ui.viewmodels.drink

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinkDetailViewModel @Inject constructor(repo: DrinkRepository, authRepo: AuthService) :
    BaseDrinkViewModel(repo) {

    val drink = MutableLiveData<Drink>()

    // fetches a single drink
    fun getDrinkById(id: String) {
        viewModelScope.launch {
            val res = safeApiCall { repo.getDrinkById(id) }
            res?.let {
                drink.value = it
            }
        }
    }

    // deletes a drink
    fun deleteDrink(id: String) {
        viewModelScope.launch {
            safeApiCall { repo.deleteDrink(id) }
            finish.emit(Unit)
        }
    }

    // updates the drink, favorite field
    fun favDrink(id: String, fav: Boolean) {
        viewModelScope.launch {
            safeApiCall { repo.favDrink(id, fav) }
            btnFavClicked.emit(Unit)
        }
    }

    // returns the drink favourite field
    fun isFav(): Boolean? {
        return drink.value?.favorite
    }

    // refetches the drink
    fun onRefresh(id: String) {
        getDrinkById(id)
    }
}