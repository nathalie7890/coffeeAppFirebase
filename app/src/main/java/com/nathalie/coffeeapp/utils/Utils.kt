package com.nathalie.coffeeapp.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Color.parseColor
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.nathalie.coffeeapp.R

// Object that contains various utility functions(used in DrinksFragment)
object Utils {
    // Changes the button text and background colours
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

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}