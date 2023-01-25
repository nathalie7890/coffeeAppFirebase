package com.nathalie.coffeeapp.viewmodels.roast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.Roast
import com.nathalie.coffeeapp.repository.RoastRepository
import kotlinx.coroutines.launch

class AddRoastViewModel(private val repo: RoastRepository) : ViewModel() {
    fun addRoast(roast: Roast) {
        viewModelScope.launch {
            repo.addRoast(roast)
        }
    }

    class Provider(private val repo: RoastRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddRoastViewModel(repo) as T
        }
    }
}