package com.nathalie.coffeeapp.viewmodels

import androidx.lifecycle.MutableLiveData
import com.nathalie.coffeeapp.data.model.User
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.ui.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val auth: AuthService) : BaseViewModel() {
    val user: MutableLiveData<User> = MutableLiveData()

    suspend fun getCurrentUser() {
        val res = safeApiCall { auth.getCurrentUser() }
        res.let {
            user.value = it
        }
    }
}