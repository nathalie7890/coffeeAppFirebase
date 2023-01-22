package com.nathalie.coffeeapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.Roast
import com.nathalie.coffeeapp.repository.RoastRepository
import kotlinx.coroutines.launch

class RoastViewModel(private val repo: RoastRepository): ViewModel() {
    val roasts : MutableLiveData<List<Roast>> = MutableLiveData()

    init {
        getRoasts()
    }

    fun getRoasts() {
        viewModelScope.launch {
            var res = repo.getRoasts()
            roasts.value = res
        }
    }

    class Provider(val repo: RoastRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RoastViewModel(repo) as T
        }
    }
}