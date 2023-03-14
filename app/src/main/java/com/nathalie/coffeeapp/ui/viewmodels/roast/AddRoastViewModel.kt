package com.nathalie.coffeeapp.ui.viewmodels.roast

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.repository.fireStoreRepo.RoastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddRoastViewModel @Inject constructor(
    repo: RoastRepository,
    private val authRepo: AuthService
) : BaseRoastViewModel(repo) {

    // Function to check input values, create image name, store image into firebase storage, create and add drink object to firestore.
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
                val uid = authRepo.getUid()
                if (uid != null) {
                    safeApiCall {
                        repo.addRoast(
                            roast.copy(image = imageName, uid = uid)
                        )
                    }
                    finish.emit(Unit)
                }
            } else {
                error.emit("Kindly provide all information")
            }
        }
    }
}