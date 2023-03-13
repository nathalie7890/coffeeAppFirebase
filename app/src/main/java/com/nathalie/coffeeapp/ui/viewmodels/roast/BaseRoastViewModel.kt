package com.nathalie.coffeeapp.ui.viewmodels.roast

import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.repository.fireStoreRepo.RoastRepository
import com.nathalie.coffeeapp.ui.utils.Utils
import com.nathalie.coffeeapp.ui.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class BaseRoastViewModel(val repo: RoastRepository) : BaseViewModel() {
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun validate(
        title: String,
        details: String,
    ): Boolean {
        return if (Utils.validate(title, details)) {
            true
        } else {
            viewModelScope.launch {
                error.emit("")
            }
            false
        }
    }
}