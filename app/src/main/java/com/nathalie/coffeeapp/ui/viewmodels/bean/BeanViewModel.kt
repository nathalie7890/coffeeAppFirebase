package com.nathalie.coffeeapp.ui.viewmodels.bean

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.repository.fireStoreRepo.BeanRepository
import com.nathalie.coffeeapp.ui.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeanViewModel @Inject constructor(
    private val repo: BeanRepository,
) : BaseViewModel() {
    val beans: MutableLiveData<List<Bean>> = MutableLiveData()

    fun getBeans() {
        viewModelScope.launch {
            val res = safeApiCall { repo.getAllBeans() }
            res?.let {
                beans.value = it
                Log.d("debugging", "get beans")
            }
        }
    }

    fun onRefresh() {
        getBeans()
    }

}