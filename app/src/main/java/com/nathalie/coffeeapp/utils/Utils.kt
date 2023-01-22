package com.nathalie.coffeeapp.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Color.parseColor
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.nathalie.coffeeapp.R

object Utils {
    fun updateColors(context: Context, view: MaterialButton, vararg views: MaterialButton) {
        view.run {
            setBackgroundColor(ContextCompat.getColor(context, R.color.chestnut))
            setTextColor(ContextCompat.getColor(context, R.color.almond))
        }

        views.forEach {
            it.setBackgroundColor(ContextCompat.getColor(context, R.color.beige))
            it.setTextColor(ContextCompat.getColor(context, R.color.chocolate))
        }
    }
}