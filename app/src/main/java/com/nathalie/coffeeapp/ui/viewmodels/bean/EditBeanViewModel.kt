package com.nathalie.coffeeapp.ui.viewmodels.bean

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.repository.fireStoreRepo.BeanRepository
import com.nathalie.coffeeapp.ui.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditBeanViewModel @Inject constructor(repo: BeanRepository) : BaseBeanViewModel(repo) {
    val bean = MutableLiveData<Bean>()

    // fetches one bean
    fun getBeanById(id: String) {
        viewModelScope.launch {
            val res = safeApiCall { repo.getBeanById(id) }
            res?.let {
                bean.value = it
            }
        }
    }

    // edits a single bean
    fun editBean(id: String, bean: Bean, imageUri: Uri?) {
        val validationStatus = Utils.validate(
            bean.title,
            bean.subtitle,
            bean.taste,
            bean.details,
        )

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH)
        val date = Date()
        val imageName: String = bean.image ?: formatter.format(date)

        if (validationStatus) {
            if (imageUri != null) {
                StorageService.addImage(imageUri, imageName) { status ->
                    if (!status) {
                        viewModelScope.launch {
                            error.emit("Image Upload Failed")
                        }
                    } else {
                        viewModelScope.launch {
                            safeApiCall { repo.updateBean(id, bean.copy(image = imageName)) }
                            finish.emit(Unit)
                        }
                    }
                }
            } else {
                viewModelScope.launch {
                    safeApiCall { repo.updateBean(id, bean.copy(image = imageName)) }
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