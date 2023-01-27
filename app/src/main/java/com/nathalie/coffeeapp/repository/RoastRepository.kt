package com.nathalie.coffeeapp.repository

import com.nathalie.coffeeapp.data.CoffeeDao
import com.nathalie.coffeeapp.data.model.Roast

// class that sends function requests to the DAO queries
class RoastRepository(private val coffeeDao:CoffeeDao) {
    // fetch all Roasts
    suspend fun getRoasts(): List<Roast> {
        return coffeeDao.getRoasts().toList()
    }

    // add new Roast
    suspend fun addRoast(roast: Roast) {
        coffeeDao.insertRoast(roast)
    }

    // find Roast that match ID
    suspend fun getRoastById(id: Long): Roast? {
        return coffeeDao.getRoastById(id)
    }

    // find Roast by ID and UPDATE
    suspend fun updateRoast(id: Long, roast: Roast) {
        coffeeDao.insertRoast(roast.copy(id = id))
    }

    // DELETE Roast by ID
    suspend fun deleteRoast(id: Long) {
        coffeeDao.deleteRoast(id)
    }
}