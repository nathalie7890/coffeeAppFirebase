package com.nathalie.coffeeapp.ui.viewmodels.roast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast
import com.nathalie.coffeeapp.repository.fireStoreRepo.RoastRepository
import com.nathalie.coffeeapp.ui.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRoastViewModel @Inject constructor(repo: RoastRepository) : BaseRoastViewModel(repo) {
    val roast = MutableLiveData<Roast>()

    fun getRoastById(id: String) {
        viewModelScope.launch {
            val res = safeApiCall { repo.getRoastById(id) }
            res?.let {
                roast.value = it
            }
        }
    }

    fun editRoast(id: String, roast: Roast) {
        val validationStatus = Utils.validate(
            roast.title, roast.details
        )
        viewModelScope.launch {
            if (validationStatus) {
                safeApiCall { repo.updateRoast(id, roast) }
                finish.emit(Unit)
            } else {
                viewModelScope.launch {
                    error.emit("Kindly provide all information")
                }
            }
        }
    }

    fun deleteRoast(id: String) {
        viewModelScope.launch {
            safeApiCall { repo.deleteRoast(id) }
            finish.emit(Unit)
        }
    }
}