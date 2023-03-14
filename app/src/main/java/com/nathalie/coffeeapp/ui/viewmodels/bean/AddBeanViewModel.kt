package com.nathalie.coffeeapp.ui.viewmodels.bean

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.repository.fireStoreRepo.BeanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddBeanViewModel @Inject constructor(
    repo: BeanRepository,
    private val authRepo: AuthService
) : BaseBeanViewModel(repo) {
    fun addBean(
        bean: Bean,
        imageUri: Uri?
    ) {
        val validationStatus = validate(
            bean.title,
            bean.subtitle,
            bean.details,
            bean.taste,
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
                val uid = authRepo.getUid()
                if (uid != null) {
                    safeApiCall {
                        repo.addBean(
                            bean.copy(
                                image = imageName, uid = uid
                            )
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