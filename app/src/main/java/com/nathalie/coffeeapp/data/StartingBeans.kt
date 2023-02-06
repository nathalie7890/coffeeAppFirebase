package com.nathalie.coffeeapp.data

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.data.model.Drink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader

class StartingBeans(private val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        Log.d("debug", "hello")
        CoroutineScope(Dispatchers.IO).launch {
            fillWithStartingBeans(context)
        }
    }

    private fun loadJSONArray(context: Context): JSONArray {

        val inputStream = context.resources.openRawResource(R.raw.bean)

        BufferedReader(inputStream.reader()).use {
            return JSONArray(it.readText())
        }
    }

    private suspend fun fillWithStartingBeans(context: Context) {
        val coffeeDao = CoffeeDatabase.getInstance(context).coffeeDao

        try {
            val drinks = loadJSONArray(context)
            for (i in 0 until drinks.length()) {
                val item = drinks.getJSONObject(i)
                val id = item.getLong("id")
                val title = item.getString("title")
                val subtitle = item.getString("subtitle")
                val taste = item.getString("taste")
                val details = item.getString("details")
                val body = item.getInt("body")
                val aroma = item.getInt("aroma")
                val caffeine = item.getInt("caffeine")
                val defaultImage = item.getString("defaultImage")

                val bean = Bean(
                    id,
                    title,
                    subtitle,
                    taste,
                    details,
                    body,
                    aroma,
                    caffeine,
                    image = null,
                    defaultImage
                )

                coffeeDao.insertBean(bean)
            }
        } catch (e: JSONException) {
//            Timber.d("fillWithStartingNotes: $e")
        }
    }
}