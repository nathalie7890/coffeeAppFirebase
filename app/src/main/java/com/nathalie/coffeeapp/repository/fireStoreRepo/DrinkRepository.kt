package com.nathalie.coffeeapp.repository.fireStoreRepo

import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink


interface DrinkRepository {
    suspend fun getAllDrinks(cat: Int): List<Drink>
    suspend fun getDrinkById(id: String): Drink?
    suspend fun addDrink(drink: Drink)
    suspend fun updateDrink(id: String, drink: Drink): Drink?
    suspend fun deleteDrink(id: String)
}