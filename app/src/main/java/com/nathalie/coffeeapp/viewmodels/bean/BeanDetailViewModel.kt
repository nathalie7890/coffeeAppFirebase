package com.nathalie.coffeeapp.viewmodels.bean

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.repository.BeanRepository
import kotlinx.coroutines.launch

class BeanDetailViewModel(private val repo: BeanRepository): ViewModel() {
    val bean: MutableLiveData<Bean> = MutableLiveData()

    //find bean that matches the id
    fun getBeanById(id: Long) {
        viewModelScope.launch {
            val res = repo.getBeanById(id)
            res?.let {
                bean.value = it
            }
        }
    }

    //delete bean that matches the id
    fun deleteBean(id: Long) {
        viewModelScope.launch {
            repo.deleteBean(id)
        }
    }

    class Provider(val repo: BeanRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BeanDetailViewModel(repo) as T
        }
    }
}