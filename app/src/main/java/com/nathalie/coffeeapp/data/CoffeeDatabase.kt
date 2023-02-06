package com.nathalie.coffeeapp.data

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.data.model.Drink
import com.nathalie.coffeeapp.data.model.Roast

// Defines the Entities (Drink, Bean, Roast) and version(must be >= 1 and must increase everytime there is an update to the Entity), to be used by the RoomDatabase(RD)
@Database(entities = [Drink::class, Bean::class, Roast::class], version = 3)
abstract class CoffeeDatabase : RoomDatabase() {
    abstract val coffeeDao: CoffeeDao

    companion object {
        const val DATABASE_NAME = "coffee_database"

        @Volatile
        private var instance: CoffeeDatabase? = null
        fun getInstance(context: Context): CoffeeDatabase {
            if (instance == null) {
                synchronized(CoffeeDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CoffeeDatabase::class.java,
                        "drinks"
                    )
                        .addCallback(StartingDrinks(context))
                        .addCallback(StartingBeans(context))
                        .addCallback(StartingRoasts(context))
                        .build()
                }
            }
            return instance!!
        }
    }
}
