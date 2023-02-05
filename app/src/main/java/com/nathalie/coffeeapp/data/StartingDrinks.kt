package com.nathalie.coffeeapp.data

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.Drink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader

class StartingDrinks(private val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            fillWithStartingNotes(context)
        }
    }

    private fun loadJSONArray(context: Context): JSONArray {

        val inputStream = context.resources.openRawResource(R.raw.drinks)

        BufferedReader(inputStream.reader()).use {
            return JSONArray(it.readText())
        }
    }

    private suspend fun fillWithStartingNotes(context: Context) {
        val coffeeDao = CoffeeDatabase.getInstance(context)?.coffeeDao

        try {
            val drinks = loadJSONArray(context)
            for (i in 0 until drinks.length()) {
                val item = drinks.getJSONObject(i)
                val id = item.getLong("id")
                val title = item.getString("title")
                val subtitle = item.getString("subtitle")
                val details = item.getString("details")
                val ingredients = item.getString("ingredients")
                val category = item.getInt("category")
                val favorite = item.getBoolean("favorite")
                val image = item.getString("image").toByteArray()
                val defaultImage = item.getString("defaultImage")

                val drink = Drink(
                    id,
                    title,
                    subtitle,
                    ingredients,
                    details,
                    category,
                    favorite,
                    image,
                    defaultImage
                )

                coffeeDao?.insert(drink)
            }
        } catch (e: JSONException) {
//            Timber.d("fillWithStartingNotes: $e")
        }
    }
}