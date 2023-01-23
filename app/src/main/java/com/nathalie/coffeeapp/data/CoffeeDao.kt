package com.nathalie.coffeeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.data.model.Drink
import com.nathalie.coffeeapp.data.model.Roast

@Dao
interface CoffeeDao {
    @Query("SELECT * FROM drink")
    suspend fun getDrinks(): List<Drink>

    @Query("SELECT * FROM drink where id= :id")
    suspend fun getDrinkById(id: Long): Drink?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drink: Drink)

    @Query("UPDATE drink SET favorite = :status WHERE id=:id")
    suspend fun favDrink(id: Long, status: Boolean)

    @Query("DELETE FROM drink WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM drink WHERE title = :title2")
    suspend fun getDrinkByTitle(title2: String): List<Drink>

    @Query("SELECT * FROM bean")
    suspend fun getBeans(): List<Bean>

    @Query("SELECT * FROM bean where id= :id")
    suspend fun getBeanById(id: Long): Bean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBean(bean: Bean)

    @Query("DELETE FROM bean WHERE id = :id")
    suspend fun deleteBean(id: Long)

    @Query("SELECT * FROM roast")
    suspend fun getRoasts(): List<Roast>

    @Query("SELECT * FROM roast where id= :id")
    suspend fun getRoastById(id: Long): Roast?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoast(roast: Roast)

    @Query("DELETE FROM roast WHERE id = :id")
    suspend fun deleteRoast(id: Long)
}