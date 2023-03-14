package com.nathalie.coffeeapp.repository.fireStoreRepo

import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast

// Repository to send functions to Firebase FireStoreRoastRepository
interface RoastRepository {

    // Fetches all documents in roasts collection
    suspend fun getAllRoasts(uid: String): List<Roast>

    // fetches one document in roasts collection
    suspend fun getRoastById(id: String): Roast?

    // adds one document to roasts collection
    suspend fun addRoast(roast: Roast)

    // edits one document in roasts collection
    suspend fun updateRoast(id: String, roast: Roast): Roast?

    // deletes one document in roasts collection
    suspend fun deleteRoast(id: String)
}