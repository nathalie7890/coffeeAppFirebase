package com.nathalie.coffeeapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.nathalie.coffeeapp.data.DrinkDatabase
import com.nathalie.coffeeapp.repository.DrinkRepository

class MyApplication: Application() {
    lateinit var drinkRepo: DrinkRepository

    override fun onCreate() {
        super.onCreate()

        val drinkDatabase = Room.databaseBuilder(
            this,
           DrinkDatabase::class.java,
            DrinkDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

       drinkRepo = DrinkRepository(drinkDatabase.drinkDao)
    }
}