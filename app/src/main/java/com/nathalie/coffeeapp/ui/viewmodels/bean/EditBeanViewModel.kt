package com.nathalie.coffeeapp.ui.viewmodels.bean

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.repository.fireStoreRepo.BeanRepository
import com.nathalie.coffeeapp.ui.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditBeanViewModel @Inject constructor(repo: BeanRepository) : BaseBeanViewModel(repo) {
    val bean = MutableLiveData<Bean>()

    fun getBeanById(id: String) {
        viewModelScope.launch {
            val res = safeApiCall { repo.getBeanById(id) }
            res?.let {
                bean.value = it
            }
        }
    }

    fun editBean(id: String, bean: Bean) {
        val validationStatus = Utils.validate(
            bean.title,
            bean.subtitle,
            bean.taste,
            bean.details,
        )
        viewModelScope.launch {
            if (validationStatus) {
                safeApiCall { repo.updateBean(id, bean) }
                finish.emit(Unit)
            } else {
                viewModelScope.launch {
                    error.emit("Kindly provide all information")
                }
            }
        }
    }
}