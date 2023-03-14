package com.nathalie.coffeeapp.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.data.service.AuthService

import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import kotlinx.coroutines.tasks.await

class FireStoreDrinkRepository(
    private val ref: CollectionReference,
) : DrinkRepository {
    override suspend fun getAllDrinks(
        search: String,
        cat: Int,
        fav: Int,
        uid: String
    ): List<Drink> {
        val drinks = mutableListOf<Drink>()
        val res: QuerySnapshot
        if (fav == 1) {
            res = ref.whereEqualTo("uid", uid).whereEqualTo("favorite", fav).get()
                .await()
        } else if (cat == 1) {
            res =
                ref.whereEqualTo("uid", uid).whereEqualTo("category", 1).get().await()
        } else if (cat == 2) {
            res =
                ref.whereEqualTo("uid", uid).whereEqualTo("category", 2).get().await()
        } else {
            res = ref.whereEqualTo("uid", uid).get().await()
        }

        for (document in res) {
            drinks.add(document.toObject(Drink::class.java).copy(id = document.id))
        }

        val defaultDrinks = ref.whereEqualTo("uid", "default").get().await()
        for (document in defaultDrinks) {
            drinks.add(document.toObject(Drink::class.java).copy(id = document.id))
        }

        return drinks.filter {
            Regex(
                search,
                RegexOption.IGNORE_CASE
            ).containsMatchIn(it.title)
        }
    }

    override suspend fun getDrinkById(id: String): Drink? {
        val res = ref.document(id).get().await()
        return res.toObject(Drink::class.java)?.copy(id = id)
    }

    override suspend fun addDrink(drink: Drink) {
        ref.add(drink).await()
    }

    override suspend fun deleteDrink(id: String) {
        ref.document(id).delete().await()
    }

    override suspend fun updateDrink(id: String, drink: Drink): Drink? {
//        val update = drink.copy(id = id)
        ref.document(id).set(drink).await()
        return null
    }

    override suspend fun favDrink(id: String, fav: Int) {
        ref.document(id).update("favorite", fav).await()
    }
}