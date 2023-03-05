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
class SignupViewModel @Inject constructor(private val authRepo: AuthService) : BaseViewModel() {
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun signUp(name: String, email: String, pass: String, confirmPass: String) {
        if (Utils.validate(name, email, pass, confirmPass) && pass == confirmPass) {
            viewModelScope.launch {
                safeApiCall {
                    authRepo.createUser(User(name, email, pass))
                    finish.emit(Unit)
                }
            }
        } else {
            viewModelScope.launch {
                error.emit("Please provide all information")
            }
        }
    }
}