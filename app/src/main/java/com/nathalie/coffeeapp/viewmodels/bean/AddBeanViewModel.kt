package com.nathalie.coffeeapp.viewmodels.bean

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.repository.BeanRepository
import kotlinx.coroutines.launch

class AddBeanViewModel(private val repo: BeanRepository) : ViewModel() {
    fun addBean(bean: Bean) {
        viewModelScope.launch {
            repo.addBean(bean)
        }
    }

    class Provider(private val repo: BeanRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddBeanViewModel(repo) as T
        }
    }
}