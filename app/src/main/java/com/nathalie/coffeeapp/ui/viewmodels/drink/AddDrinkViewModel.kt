package com.nathalie.coffeeapp.ui.viewmodels.drink

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddDrinkViewModel @Inject constructor(repo: DrinkRepository) : BaseDrinkViewModel(repo) {
    fun addDrink(
        drink: Drink,
        imageUri: Uri?
    ) {
        val validationStatus = validate(
            drink.title,
            drink.subtitle,
            drink.details,
            drink.ingredients,
            drink.category.toString(),
        )

        val formatter = SimpleDateFormat("yyyy_MM_HH_mm_ss", Locale.ENGLISH)
        val date = Date()
        val imageName = formatter.format(date)

        viewModelScope.launch {
            imageUri?.let {
                StorageService.addImage(it, imageName) { status ->
                    if (!status) {
                        viewModelScope.launch {
                            error.emit("Image failed!")
                        }
                    }
                }
            }
            if (validationStatus) {
                safeApiCall { repo.addDrink(drink.copy(image = imageName)) }
                finish.emit(Unit)
            } else {
                error.emit("Kindly provide all information")
            }
        }
    }
}