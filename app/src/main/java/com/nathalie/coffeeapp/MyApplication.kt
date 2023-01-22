package com.nathalie.coffeeapp

import android.app.Application
import androidx.room.Room
import com.nathalie.coffeeapp.data.CoffeeDatabase
import com.nathalie.coffeeapp.repository.BeanRepository
import com.nathalie.coffeeapp.repository.DrinkRepository
import com.nathalie.coffeeapp.repository.RoastRepository

class MyApplication : Application() {
    lateinit var drinkRepo: DrinkRepository
    lateinit var beanRepo: BeanRepository
    lateinit var roastRepo: RoastRepository
    override fun onCreate() {
        super.onCreate()

        val coffeeDatabase = Room.databaseBuilder(
            this,
            CoffeeDatabase::class.java,
            CoffeeDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

        drinkRepo = DrinkRepository(coffeeDatabase.coffeeDao)
        beanRepo = BeanRepository(coffeeDatabase.coffeeDao)
        roastRepo = RoastRepository(coffeeDatabase.coffeeDao)
    }
}