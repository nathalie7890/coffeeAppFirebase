package com.nathalie.coffeeapp.repository

import com.nathalie.coffeeapp.data.CoffeeDao
import com.nathalie.coffeeapp.data.model.Drink

class DrinkRepository(private val coffeeDao: CoffeeDao) {

    //fetch drinks
    suspend fun getDrinks(str: String, cat: Int): List<Drink> {
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

    //add drink
    suspend fun addDrink(drink: Drink) {
        coffeeDao.insert(drink)
    }

    //find drink that match the given id
    suspend fun getDrinkById(id: Long): Drink? {
        return coffeeDao.getDrinkById(id)
    }


    //find drink by id and update it
    suspend fun updateDrink(id: Long, drink: Drink) {
        coffeeDao.insert(drink.copy(id = id))
    }

    //delete drink by id
    suspend fun deleteDrink(id: Long) {
        coffeeDao.delete(id)
    }

    //find drink by title
    suspend fun getDrinkByTitle(title: String): List<Drink> {
        return coffeeDao.getDrinkByTitle(title)
    }
}