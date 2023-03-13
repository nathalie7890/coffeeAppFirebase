package com.nathalie.coffeeapp.ui.viewmodels.roast

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.repository.fireStoreRepo.RoastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddRoastViewModel @Inject constructor(repo: RoastRepository) : BaseRoastViewModel(repo) {

    fun addRoast(roast: Roast, imageUri: Uri?) {
        val validationStatus = validate(roast.title, roast.details)

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
                safeApiCall { repo.addRoast(roast.copy(image = imageName)) }
                finish.emit(Unit)
            } else {
                error.emit("Kindly provide all information")
            }
        }
    }
}