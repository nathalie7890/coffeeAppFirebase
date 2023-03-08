package com.nathalie.coffeeapp.repository.fireStoreRepo

import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast

interface RoastRepository {
    suspend fun getAllRoasts(): List<Roast>

    suspend fun getRoastById(id: String): Roast?

    suspend fun addRoast(roast: Roast)

    suspend fun updateRoast(id: String, roast: Roast): Roast?

    suspend fun deleteRoast(id: String)
}