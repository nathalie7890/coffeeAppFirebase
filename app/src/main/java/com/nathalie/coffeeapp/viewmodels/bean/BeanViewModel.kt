package com.nathalie.coffeeapp.viewmodels.bean

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.repository.BeanRepository
import kotlinx.coroutines.launch

class BeanViewModel(private val repo: BeanRepository) : ViewModel() {
    val beans: MutableLiveData<List<Bean>> = MutableLiveData()

    init {
        getBeans()
    }

    fun getBeans() {
        viewModelScope.launch {
            var res = repo.getBeans()
            beans.value = res
        }
    }

    class Provider(val repo: BeanRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BeanViewModel(repo) as T
        }
    }
}