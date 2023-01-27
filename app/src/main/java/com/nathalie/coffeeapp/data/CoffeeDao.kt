package com.nathalie.coffeeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.data.model.Drink
import com.nathalie.coffeeapp.data.model.Roast
import kotlinx.coroutines.flow.Flow

// Data Access Object(DAO) to make requests/queries from the RoomDatabase(RD) for the Bean, Drink, and Roast Entities
@Dao
interface CoffeeDao {
    // Fetches all the data from Drink and stores it as a list of Drinks
    @Query("SELECT * FROM drink")
    suspend fun getDrinks(): List<Drink>

    // Fetches a single data from Drink where the ID's match
    @Query("SELECT * FROM drink where id= :id")
    fun getDrinkById(id: Long): Flow<Drink?>

    // ADD new data into Drink or UPDATE existing data in Drink
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drink: Drink)

    // Changes Drink column favorite to status where ID's match
    @Query("UPDATE drink SET favorite = :status WHERE id=:id")
    suspend fun favDrink(id: Long, status: Boolean)

    // DELETE existing data from Drink where ID's match
    @Query("DELETE FROM drink WHERE id = :id")
    suspend fun delete(id: Long)

    // Fetches all data from Drink where Title's match
    @Query("SELECT * FROM drink WHERE title = :title2")
    suspend fun getDrinkByTitle(title2: String): List<Drink>

    // Fetches all the data from Bean and stores it as a list of Beans
    @Query("SELECT * FROM bean")
    suspend fun getBeans(): List<Bean>

    // Fetches a single data from Bean where the ID's match
    @Query("SELECT * FROM bean where id= :id")
    suspend fun getBeanById(id: Long): Bean?

    // ADD new data into Drink or UPDATE existing data in Drink
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBean(bean: Bean)

    // DELETE existing data from Bean where ID's match
    @Query("DELETE FROM bean WHERE id = :id")
    suspend fun deleteBean(id: Long)

    // Fetches all the data from Roast and stores it as a list of Roasts
    @Query("SELECT * FROM roast")
    suspend fun getRoasts(): List<Roast>

    // Fetches a single data from Roast where the ID's match
    @Query("SELECT * FROM roast where id= :id")
    suspend fun getRoastById(id: Long): Roast?

    // ADD new data into Roast or UPDATE existing data in Roast
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoast(roast: Roast)

    // DELETE existing data from Roast where ID's match
    @Query("DELETE FROM roast WHERE id = :id")
    suspend fun deleteRoast(id: Long)
}