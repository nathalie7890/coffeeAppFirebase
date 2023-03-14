package com.nathalie.coffeeapp.ui.presentation.user.viewmodels

import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.data.model.User
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.ui.utils.Utils
import com.nathalie.coffeeapp.ui.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
// Holds all the functions for login
class LoginViewModel @Inject constructor(private val auth: AuthService) : BaseViewModel() {
    val loginFinish: MutableSharedFlow<Unit> = MutableSharedFlow()

    // Receive the email and password to login
    fun login(email: String, pass: String) {
        if (Utils.validate(email, pass)) {
            viewModelScope.launch {
                safeApiCall {
                    auth.login(email, pass)
                    loginFinish.emit(Unit)
                }
            }
        } else {
            viewModelScope.launch {
                error.emit("Fail to login")
            }
        }
    }

    // Fetches the current logged in user object
    suspend fun getCurrentUser(): User? {
        return safeApiCall { auth.getCurrentUser() }
    }
}