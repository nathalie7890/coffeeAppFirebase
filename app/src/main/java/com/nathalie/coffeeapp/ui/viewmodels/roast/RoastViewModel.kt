package com.nathalie.coffeeapp.ui.viewmodels.roast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.repository.fireStoreRepo.RoastRepository
import com.nathalie.coffeeapp.ui.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoastViewModel @Inject constructor(
    private val repo: RoastRepository,
    private val authRepo: AuthService
) : BaseViewModel() {
    val roasts: MutableLiveData<List<Roast>> = MutableLiveData()
    val swipeRefreshLayoutFinished: MutableSharedFlow<Unit> = MutableSharedFlow()

    // refetch roasts
    fun onRefresh() {
        viewModelScope.launch {
            getRoasts()
            swipeRefreshLayoutFinished.emit(Unit)
        }
    }

    // fetches all roasts
    suspend fun getRoasts() {
        val uid = authRepo.getUid()
        if (uid != null) {
            val res = safeApiCall { repo.getAllRoasts(uid) }
            res?.let {
                roasts.value = it
            }
        }
    }
}