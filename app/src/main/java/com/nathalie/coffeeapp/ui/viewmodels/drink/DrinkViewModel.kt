package com.nathalie.coffeeapp.ui.viewmodels.drink

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import com.nathalie.coffeeapp.ui.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinkViewModel @Inject constructor(
    private val repo: DrinkRepository,
    private val authRepo: AuthService
) : BaseViewModel() {
    val drinks: MutableLiveData<List<Drink>> = MutableLiveData()
    val swipeRefreshLayoutFinished: MutableSharedFlow<Unit> = MutableSharedFlow()

    override fun onViewCreated() {
        super.onViewCreated()
        viewModelScope.launch {
            getDrinks("", 0, 0)
        }
    }

    fun onRefresh(search: String, cat: Int, fav: Int) {
        viewModelScope.launch {
//            delay(1000)
            getDrinks(search, cat, fav)
            swipeRefreshLayoutFinished.emit(Unit)
        }
    }


    suspend fun getDrinks(search: String, cat: Int, fav: Int) {
        val uid = authRepo.getUid()
        if (uid != null) {
            val res = safeApiCall { repo.getAllDrinks(search, cat, fav, uid) }
            res?.let {
                drinks.value = it
            }

        }
    }
}