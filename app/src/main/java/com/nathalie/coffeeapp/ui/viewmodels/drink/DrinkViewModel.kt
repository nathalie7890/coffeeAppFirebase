package com.nathalie.coffeeapp.ui.viewmodels.drink

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

//    override fun onViewCreated() {
//        super.onViewCreated()
//        getDrinks(0)
//    }

    fun onRefresh(cat:Int) {
        viewModelScope.launch {
            delay(1000)
            getDrinks(cat)
            swipeRefreshLayoutFinished.emit(Unit)
        }
    }

    fun logout() {
        authRepo.deAuthenticate()
    }

    fun getDrinks(cat:Int) {
        viewModelScope.launch {
            val res = safeApiCall { repo.getAllDrinks(cat) }
            res?.let {
                drinks.value = it
            }
        }
    }
}