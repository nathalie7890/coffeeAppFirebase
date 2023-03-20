package com.nathalie.coffeeapp.repository.fireStoreRepo

import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink

// Repository to send functions to Firebase FireStoreDrinkRepository
interface DrinkRepository {

    // Fetches all documents in drinks collection
    suspend fun getAllDrinks(search: String, cat: Int, fav: Boolean, uid: String): List<Drink>

    // Fetches one document in drinks collection
    suspend fun getDrinkById(id: String): Drink?

    // Adds one document in drinks collection
    suspend fun addDrink(drink: Drink)

    // Edits one document in drinks collection
    suspend fun updateDrink(id: String, drink: Drink): Drink?

    // Delete one document in drinks collection
    suspend fun deleteDrink(id: String)

    // Edits fav field in one document in drinks collection
    suspend fun favDrink(id: String, fav: Boolean)
}