package com.nathalie.coffeeapp.repository.fireStoreRepo

import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink


interface DrinkRepository {
    suspend fun getAllDrinks(search: String, cat: Int, fav: Int, uid:String): List<Drink>
    suspend fun getDrinkById(id: String): Drink?
    suspend fun addDrink(drink: Drink)
    suspend fun updateDrink(id: String, drink: Drink): Drink?
    suspend fun deleteDrink(id: String)
    suspend fun favDrink(id: String, fav: Int)
}