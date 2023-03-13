package com.nathalie.coffeeapp.ui.viewmodels.roast

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.repository.fireStoreRepo.RoastRepository
import com.nathalie.coffeeapp.ui.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
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

    fun editRoast(id: String, roast: Roast, imageUri: Uri?) {
        val validationStatus = Utils.validate(
            roast.title, roast.details
        )

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH)
        val date = Date()
        val imageName: String = roast.image ?: formatter.format(date)

        if (validationStatus) {
            if (imageUri != null) {
                StorageService.addImage(imageUri, imageName) { status ->
                    if (!status) {
                        viewModelScope.launch {
                            error.emit("Image Upload Failed")
                        }
                    } else {
                        viewModelScope.launch {
                            safeApiCall { repo.updateRoast(id, roast.copy(image = imageName)) }
                            finish.emit(Unit)
                        }
                    }
                }
            } else {
                viewModelScope.launch {
                    safeApiCall { repo.updateRoast(id, roast.copy(image = imageName)) }
                    finish.emit(Unit)
                }
            }
        } else {
            viewModelScope.launch {
                error.emit("Kindly provide all information")
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