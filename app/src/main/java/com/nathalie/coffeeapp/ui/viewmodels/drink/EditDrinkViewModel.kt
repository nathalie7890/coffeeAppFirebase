package com.nathalie.coffeeapp.ui.viewmodels.drink

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import com.nathalie.coffeeapp.ui.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
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

    fun editDrink(id: String, drink: Drink, imageUri: Uri?) {
        val validationStatus = Utils.validate(
            drink.title,
            drink.subtitle,
            drink.details,
            drink.ingredients,
            drink.category.toString()
        )

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH)
        val date = Date()
        val imageName: String = drink.image ?: formatter.format(date)

        if (validationStatus) {
            if (imageUri != null) {
                StorageService.addImage(imageUri, imageName) { status ->
                    if (!status) {
                        viewModelScope.launch {
                            error.emit("Image Upload Failed")
                        }
                    } else {
                        viewModelScope.launch {
                            safeApiCall { repo.updateDrink(id, drink.copy(image = imageName)) }
                            finish.emit(Unit)
                        }
                    }
                }
            } else {
                viewModelScope.launch {
                    safeApiCall { repo.updateDrink(id, drink.copy(image = imageName)) }
                    finish.emit(Unit)
                }
            }
        } else {
            viewModelScope.launch {
                error.emit("Kindly provide all information")
            }
        }
    }
}