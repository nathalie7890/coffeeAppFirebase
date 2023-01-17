package com.nathalie.coffeeapp.repository

import com.nathalie.coffeeapp.data.DrinkDao
import com.nathalie.coffeeapp.data.Model.Drink

class DrinkRepository(private val drinkDao: DrinkDao) {

    //fetch drinks
    suspend fun getDrinks(str: String, cat: String): List<Drink> {
        if (cat == "") {
            return drinkDao.getDrinks().filter {
                Regex(
                    str,
                    RegexOption.IGNORE_CASE
                ).containsMatchIn(it.title)
            }.toList()
        }

        return drinkDao.getDrinks().filter {
            Regex(
                str,
                RegexOption.IGNORE_CASE
            ).containsMatchIn(it.title) && it.category == cat
        }.toList()
    }

    //add drink
    suspend fun addDrink(drink: Drink) {
        drinkDao.insert(drink)
    }

    //find drink that match the given id
    suspend fun getDrinkById(id: Long): Drink? {
        return drinkDao.getDrinkById(id)
    }


    //find drink by id and update it
    suspend fun updateDrink(id: Long, drink: Drink) {
        drinkDao.insert(drink.copy(id = id))
    }

    //delete drink by id
    suspend fun deleteDrink(id: Long) {
        drinkDao.delete(id)
    }

    //find drink by title
    suspend fun getDrinkByTitle(title: String): List<Drink> {
        return drinkDao.getDrinkByTitle(title)
    }
}