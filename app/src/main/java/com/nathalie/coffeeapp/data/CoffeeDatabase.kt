package com.nathalie.coffeeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.data.model.Drink
import com.nathalie.coffeeapp.data.model.Roast


@Database(entities = [Drink::class, Bean::class, Roast::class], version = 7)
abstract class CoffeeDatabase : RoomDatabase() {

    abstract val coffeeDao: CoffeeDao

    companion object {
        const val DATABASE_NAME = "drink_database"
    }
}