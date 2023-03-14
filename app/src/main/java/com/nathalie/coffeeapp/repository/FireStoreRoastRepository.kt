package com.nathalie.coffeeapp.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast
import com.nathalie.coffeeapp.repository.fireStoreRepo.RoastRepository
import kotlinx.coroutines.tasks.await

class FireStoreRoastRepository(private val ref: CollectionReference) : RoastRepository {
    override suspend fun getAllRoasts(uid: String): List<Roast> {
        val roasts = mutableListOf<Roast>()
        val res: QuerySnapshot = ref.whereEqualTo("uid", uid).get().await()
        val defaultRoasts = ref.whereEqualTo("uid", "default").get().await()

        for (document in res) {
            roasts.add(document.toObject(Roast::class.java).copy(id = document.id))
        }

        for (document in defaultRoasts) {
            roasts.add(document.toObject(Roast::class.java).copy(id = document.id))
        }

        return roasts
    }

    override suspend fun getRoastById(id: String): Roast? {
        val res = ref.document(id).get().await()
        return res.toObject(Roast::class.java)?.copy(id = id)
    }

    override suspend fun addRoast(roast: Roast) {
        ref.add(roast).await()
    }

    override suspend fun updateRoast(id: String, roast: Roast): Roast? {
        val update = roast.copy(id = id)
        ref.document(id).set(update).await()
        return update
    }

    override suspend fun deleteRoast(id: String) {
        ref.document(id).delete().await()
    }
}