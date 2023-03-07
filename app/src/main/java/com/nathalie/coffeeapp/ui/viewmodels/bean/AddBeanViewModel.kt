package com.nathalie.coffeeapp.ui.viewmodels.bean

import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.repository.fireStoreRepo.BeanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBeanViewModel @Inject constructor(repo: BeanRepository) : BaseBeanViewModel(repo) {
    fun addBean(
        bean: Bean
    ) {
        val validationStatus = validate(
            bean.title,
            bean.subtitle,
            bean.details,
            bean.taste,
        )

        viewModelScope.launch {
            if (validationStatus) {
                safeApiCall { repo.addBean(bean) }
                finish.emit(Unit)
            } else {
                error.emit("Kindly provide all information")
            }
        }
    }
}