package com.nathalie.coffeeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nathalie.coffeeapp.data.Model.Drink

@Dao
interface DrinkDao {
    @Query("SELECT * FROM drink")
    suspend fun getDrinks(): List<Drink>

    @Query("SELECT * FROM drink where id= :id")
    suspend fun getDrinkById(id: Long): Drink?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drink: Drink)

    @Query("DELETE FROM drink WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM drink WHERE title = :title2")
    suspend fun getDrinkByTitle(title2: String): List<Drink>
}