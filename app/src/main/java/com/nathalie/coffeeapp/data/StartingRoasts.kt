package com.nathalie.coffeeapp.data

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.Drink
import com.nathalie.coffeeapp.data.model.Roast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader

class StartingRoasts(private val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            fillWithStartingRoasts(context)
        }
    }

    private fun loadJSONArray(context: Context): JSONArray {
        val inputStream = context.resources.openRawResource(R.raw.roasts)
        BufferedReader(inputStream.reader()).use {
            return JSONArray(it.readText())
        }
    }

    private suspend fun fillWithStartingRoasts(context: Context) {
        val coffeeDao = CoffeeDatabase.getInstance(context).coffeeDao

        try {
            val drinks = loadJSONArray(context)
            for (i in 0 until drinks.length()) {
                val item = drinks.getJSONObject(i)
                val id = item.getLong("id")
                val title = item.getString("title")
                val details = item.getString("details")
                val defaultImage = item.getString("defaultImage")

                val roast = Roast(
                    id,
                    title,
                    details,
                    image = null,
                    defaultImage
                )

                coffeeDao.insertRoast(roast)
            }
        } catch (e: JSONException) {
//            Timber.d("fillWithStartingNotes: $e")
        }
    }
}