package com.nathalie.coffeeapp.ui.viewmodels.bean

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.repository.fireStoreRepo.BeanRepository
import com.nathalie.coffeeapp.ui.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeanViewModel @Inject constructor(
    private val repo: BeanRepository, private val authRepo: AuthService
) : BaseViewModel() {
    val beans: MutableLiveData<List<Bean>> = MutableLiveData()
    val isLoading: MutableSharedFlow<Boolean> = MutableSharedFlow()

    // gets all beans
    suspend fun getBeans() {
        val uid = authRepo.getUid()
        isLoading.emit(true)
        if (uid != null) {
            val res = safeApiCall { repo.getAllBeans(uid) }
            res?.let {
                beans.value = it
            }
            isLoading.emit(false)
        }
    }
}