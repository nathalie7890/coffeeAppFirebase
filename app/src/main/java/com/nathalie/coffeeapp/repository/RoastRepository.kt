package com.nathalie.coffeeapp.repository

import com.nathalie.coffeeapp.data.CoffeeDao
import com.nathalie.coffeeapp.data.model.Roast

class RoastRepository(private val coffeeDao:CoffeeDao) {
    //fetch roasts
    suspend fun getRoasts(): List<Roast> {
        return coffeeDao.getRoasts().toList()
    }

    //add roast
    suspend fun addRoast(roast: Roast) {
        coffeeDao.insertRoast(roast)
    }

    //find roast that match the given id
    suspend fun getRoastById(id: Long): Roast? {
        return coffeeDao.getRoastById(id)
    }

    //find roast by id and update it
    suspend fun updateRoast(id: Long, roast: Roast) {
        coffeeDao.insertRoast(roast.copy(id = id))
    }

    //delete roast by id
    suspend fun deleteRoast(id: Long) {
        coffeeDao.deleteRoast(id)
    }
}