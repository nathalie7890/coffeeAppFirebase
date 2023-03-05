package com.nathalie.coffeeapp.ui.viewmodels.drink

import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import com.nathalie.coffeeapp.ui.utils.Utils
import com.nathalie.coffeeapp.ui.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class BaseDrinkViewModel(val repo: DrinkRepository) : BaseViewModel() {
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun validate(
        title: String,
        subtitle: String,
        details: String,
        ingredients: String,
        category: String,
    ): Boolean {
        if (Utils.validate(
                title, subtitle, details, ingredients, category
            )
        ) {
            return true
        } else {
            viewModelScope.launch {
                error.emit("")
            }
            return false
        }
    }
}