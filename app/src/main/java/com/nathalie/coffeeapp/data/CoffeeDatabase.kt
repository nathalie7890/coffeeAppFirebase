package com.nathalie.coffeeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.data.model.Drink
import com.nathalie.coffeeapp.data.model.Roast


@Database(entities = [Drink::class, Bean::class, Roast::class], version = 1)
abstract class CoffeeDatabase : RoomDatabase() {

    abstract val coffeeDao: CoffeeDao

    companion object {
        const val DATABASE_NAME = "drink_database"

//        val MIGRATION_8_9 = object : Migration(8, 9) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE drink ADD taste INT")
//            }
//        }

    }
}