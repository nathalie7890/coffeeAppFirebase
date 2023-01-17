package com.nathalie.coffeeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nathalie.coffeeapp.data.Model.Drink


@Database(entities = [Drink::class], version = 1)
abstract class DrinkDatabase : RoomDatabase() {

    abstract val drinkDao: DrinkDao

    companion object {
        const val DATABASE_NAME = "drink_database"
    }
}