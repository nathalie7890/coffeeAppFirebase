package com.nathalie.coffeeapp.ui.viewmodels.bean

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.repository.fireStoreRepo.BeanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeanDetailViewModel @Inject constructor(repo: BeanRepository) : BaseBeanViewModel(repo) {
    val bean: MutableLiveData<Bean> = MutableLiveData()

    // fetches a single bean
    fun getBeanById(id: String) {
        viewModelScope.launch {
            val res = safeApiCall { repo.getBeanById(id) }
            res?.let {
                bean.value = it
            }
        }
    }

    // deletes a bean
    fun deleteBean(id: String) {
        viewModelScope.launch {
            safeApiCall { repo.deleteBean(id) }
            finish.emit(Unit)
        }
    }
}