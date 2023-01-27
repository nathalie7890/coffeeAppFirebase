package com.nathalie.coffeeapp.repository

import com.nathalie.coffeeapp.data.CoffeeDao
import com.nathalie.coffeeapp.data.model.Drink
import kotlinx.coroutines.flow.Flow

// class that sends function requests to the DAO queries
class DrinkRepository(private val coffeeDao: CoffeeDao) {
    // fetch all Drinks and filters the Drinks based on its Category|Favorite property
    suspend fun getDrinks(str: String, cat: Int, fav: Boolean): List<Drink> {
        if (fav) {
            return coffeeDao.getDrinks().filter {
                Regex(
                    str,
                    RegexOption.IGNORE_CASE
                ).containsMatchIn(it.title) && it.favorite == true
            }.toList()
        }

        if (cat == 0) {
            return coffeeDao.getDrinks().filter {
                Regex(
                    str,
                    RegexOption.IGNORE_CASE
                ).containsMatchIn(it.title)
            }.toList()
        }

        return coffeeDao.getDrinks().filter {
            Regex(
                str,
                RegexOption.IGNORE_CASE
            ).containsMatchIn(it.title) && it.category == cat
        }.toList()
    }

    // add new Drink
    suspend fun addDrink(drink: Drink) {
        coffeeDao.insert(drink)
    }

    // find Drink that match ID
    fun getDrinkById(id: Long): Flow<Drink?> {
        return coffeeDao.getDrinkById(id)
    }

    // find Drink by ID and UPDATE
    suspend fun updateDrink(id: Long, drink: Drink) {
        coffeeDao.insert(drink.copy(id = id))
    }

    // find Drink by ID and UPDATE Favourite property
    suspend fun favDrink(id: Long, status: Boolean) {
        coffeeDao.favDrink(id, status)
    }

    // DELETE Drink by ID
    suspend fun deleteDrink(id: Long) {
        coffeeDao.delete(id)
    }

    // find Drink by Title
    suspend fun getDrinkByTitle(title: String): List<Drink> {
        return coffeeDao.getDrinkByTitle(title)
    }
}