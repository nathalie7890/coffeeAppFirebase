package com.nathalie.coffeeapp.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink

import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import kotlinx.coroutines.tasks.await

class FireStoreDrinkRepository(
    private val ref: CollectionReference
) : DrinkRepository {
    override suspend fun getAllDrinks(search: String, cat: Int): List<Drink> {
        val drinks = mutableListOf<Drink>()
        var res: QuerySnapshot
        if (cat == 1) {
            res = ref.whereEqualTo("category", 1).get().await()
        } else if (cat == 2) {
            res = ref.whereEqualTo("category", 2).get().await()
        } else {
            res = ref.get().await()
        }

        for (document in res) {
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
        ref.add(drink.toHashMap()).await()
    }

    override suspend fun deleteDrink(id: String) {
        ref.document(id).delete().await()
    }

    override suspend fun updateDrink(id: String, drink: Drink): Drink? {
        val update = drink.copy(id = id)
        ref.document(id).set(update.toHashMap()).await()
        return update
    }

    override suspend fun favDrink(id: String, fav: Int) {
        ref.document(id).update("favorite", fav).await()
    }
}