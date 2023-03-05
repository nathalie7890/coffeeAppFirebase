package com.nathalie.coffeeapp

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.nathalie.coffeeapp.data.CoffeeDatabase
import com.nathalie.coffeeapp.data.StartingDrinks
import com.nathalie.coffeeapp.repository.BeanRepository
import com.nathalie.coffeeapp.repository.DrinkRepository
import com.nathalie.coffeeapp.repository.RoastRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    lateinit var drinkRepo: DrinkRepository
    lateinit var beanRepo: BeanRepository
    lateinit var roastRepo: RoastRepository
    override fun onCreate() {
        super.onCreate()
        val coffeeDatabase = CoffeeDatabase.getInstance(this)

        drinkRepo = DrinkRepository(coffeeDatabase.coffeeDao)
        beanRepo = BeanRepository(coffeeDatabase.coffeeDao)
        roastRepo = RoastRepository(coffeeDatabase.coffeeDao)
    }
}