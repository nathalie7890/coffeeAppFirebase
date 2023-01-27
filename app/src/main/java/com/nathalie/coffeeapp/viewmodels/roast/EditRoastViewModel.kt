package com.nathalie.coffeeapp.viewmodels.roast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.Roast
import com.nathalie.coffeeapp.repository.RoastRepository
import kotlinx.coroutines.launch

class EditRoastViewModel(private val repo: RoastRepository) : ViewModel() {
    val roast: MutableLiveData<Roast> = MutableLiveData()

    //find roast that matches the id
    fun getRoastById(id: Long) {
        viewModelScope.launch {
            val res = repo.getRoastById(id)
            res?.let {
                roast.value = it
            }
        }
    }

    //edit roast that matches the id
    fun editRoast(id: Long, roast: Roast) {
        viewModelScope.launch {
            repo.updateRoast(id, roast)
        }
    }

    fun deleteRoast(id: Long) {
        viewModelScope.launch {
            repo.deleteRoast(id)
        }
    }

    class Provider(val repo: RoastRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditRoastViewModel(repo) as T
        }
    }
}