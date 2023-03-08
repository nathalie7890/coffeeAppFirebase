package com.nathalie.coffeeapp.ui.viewmodels.roast

import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast
import com.nathalie.coffeeapp.repository.fireStoreRepo.RoastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRoastViewModel @Inject constructor(repo: RoastRepository) : BaseRoastViewModel(repo) {

    fun addRoast(roast: Roast) {
        val validationStatus = validate(roast.title, roast.details)
        viewModelScope.launch {
            if (validationStatus) {
                safeApiCall { repo.addRoast(roast) }
                finish.emit(Unit)
            } else {
                error.emit("Kindly provide all information")
            }
        }
    }
}