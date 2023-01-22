package com.nathalie.coffeeapp.viewmodels.bean

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.repository.BeanRepository
import kotlinx.coroutines.launch

class EditBeanViewModel(private val repo: BeanRepository):ViewModel() {
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

    //edit bean that matches the id
    fun editBean(id: Long, bean: Bean) {
        viewModelScope.launch {
            repo.updateBean(id, bean)
        }
    }

    class Provider(val repo: BeanRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditBeanViewModel(repo) as T
        }
    }
}