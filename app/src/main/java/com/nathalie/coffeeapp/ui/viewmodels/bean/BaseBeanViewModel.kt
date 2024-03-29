package com.nathalie.coffeeapp.ui.viewmodels.bean

import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.repository.fireStoreRepo.BeanRepository
import com.nathalie.coffeeapp.ui.utils.Utils
import com.nathalie.coffeeapp.ui.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


abstract class BaseBeanViewModel(val repo: BeanRepository) : BaseViewModel() {
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    // check if edit text fields are empty
    fun validate(
        title: String,
        subtitle: String,
        taste: String,
        details: String,
    ): Boolean {
        if (Utils.validate(title, subtitle, taste, details)) {
            return true
        } else {
            viewModelScope.launch {
                error.emit("")
            }
            return false
        }
    }
}