package com.nathalie.coffeeapp.ui.viewmodels.drink

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinkDetailViewModel @Inject constructor(repo: DrinkRepository) : BaseDrinkViewModel(repo) {

    val drink = MutableLiveData<Drink>()

    fun getDrinkById(id: String) {
        viewModelScope.launch {
            val res = safeApiCall { repo.getDrinkById(id) }
            res?.let {
                drink.value = it
            }
        }
    }

    fun deleteDrink(id: String) {
        viewModelScope.launch {
            safeApiCall { repo.deleteDrink(id) }
            finish.emit(Unit)
        }
    }

    fun favDrink(id: String, fav: Int) {
        viewModelScope.launch {
            safeApiCall { repo.favDrink(id, fav) }
            btnFavClicked.emit(Unit)
        }
    }

    fun isFav(): Int? {
        return drink.value?.favorite
    }

    fun onRefresh(id: String) {
        getDrinkById(id)
    }
}