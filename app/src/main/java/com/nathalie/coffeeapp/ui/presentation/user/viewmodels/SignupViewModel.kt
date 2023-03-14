package com.nathalie.coffeeapp.ui.presentation.user.viewmodels

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.User
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.repository.fireStoreRepo.BeanRepository
import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import com.nathalie.coffeeapp.repository.fireStoreRepo.RoastRepository
import com.nathalie.coffeeapp.ui.utils.Utils
import com.nathalie.coffeeapp.ui.viewmodels.BaseViewModel
import com.nathalie.coffeeapp.ui.viewmodels.drink.AddDrinkViewModel
import com.nathalie.coffeeapp.ui.viewmodels.drink.DrinkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import javax.inject.Inject

@HiltViewModel
// Holds all the functions for login
class SignupViewModel @Inject constructor(
    private val authRepo: AuthService,
    private val drinkRepo: DrinkRepository,
    private val beanRepo: BeanRepository,
    private val roastRepo: RoastRepository
) : BaseViewModel() {
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    // Receive the name, email, pass and confirm pass to register an account
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

    //load json file
    fun loadJSONArray(context: Context, data: Int): JSONArray {
        val inputStream = context.resources.openRawResource(data)
        BufferedReader(inputStream.reader()).use {
            return JSONArray(it.readText())
        }
    }

    //add drink
    fun addDrink(drink: Drink) {
        viewModelScope.launch {
            drinkRepo.addDrink(drink)
        }
    }

    //add bean
    fun addBean(bean: Bean) {
        viewModelScope.launch {
            beanRepo.addBean(bean)
        }
    }

    //add bean
    fun addRoast(roast: Roast) {
        viewModelScope.launch {
            roastRepo.addRoast(roast)
        }
    }

    suspend fun fillWithStartingDrinks(context: Context) {
        val uid = authRepo.getUid()
        try {
            val drinks = loadJSONArray(context, R.raw.drinks)
            for (i in 0 until drinks.length()) {
                val item = drinks.getJSONObject(i)
                val id = item.getString("id")
                val title = item.getString("title")
                val subtitle = item.getString("subtitle")
                val details = item.getString("details")
                val ingredients = item.getString("ingredients")
                val category = item.getInt("category")
                val favorite = item.getInt("favorite")
                val image = item.getString("image")

                val drink = uid?.let {
                    Drink(
                        id,
                        title,
                        subtitle,
                        details,
                        ingredients,
                        category,
                        favorite,
                        image,
                        it
                    )
                }

                if (drink != null) {
                    addDrink(drink)
                }
            }
        } catch (e: JSONException) {
            viewModelScope.launch {
                error.emit("Failed to add default drinks")
            }
        }
    }

    suspend fun fillWithStartingBeans(context: Context) {
        val uid = authRepo.getUid()
        try {
            val beans = loadJSONArray(context, R.raw.bean)
            for (i in 0 until beans.length()) {
                val item = beans.getJSONObject(i)
                val id = item.getString("id")
                val title = item.getString("title")
                val subtitle = item.getString("subtitle")
                val taste = item.getString("taste")
                val details = item.getString("details")
                val body = item.getInt("body")
                val aroma = item.getInt("aroma")
                val caffeine = item.getInt("caffeine")
                val image = item.getString("image")

                val bean = uid?.let {
                    Bean(
                        id,
                        title,
                        subtitle,
                        taste,
                        details,
                        body,
                        aroma,
                        caffeine,
                        image,
                        it
                    )
                }

                if (bean != null) {
                    addBean(bean)
                }
            }
        } catch (e: JSONException) {
            viewModelScope.launch {
                error.emit("Failed to add default coffee beans")
            }
        }
    }

    suspend fun fillWithStartingRoasts(context: Context) {
        val uid = authRepo.getUid()
        try {
            val roasts = loadJSONArray(context, R.raw.roasts)
            for (i in 0 until roasts.length()) {
                val item = roasts.getJSONObject(i)
                val id = item.getString("id")
                val title = item.getString("title")
                val details = item.getString("details")
                val image = item.getString("image")

                val roast = uid?.let {
                    Roast(
                        id,
                        title,
                        details,
                        image,
                        it
                    )
                }

                if (roast != null) {
                    addRoast(roast)
                }
            }
        } catch (e: JSONException) {
            viewModelScope.launch {
                error.emit("Failed to add default coffee roasts")
            }
        }
    }
}