package com.nathalie.coffeeapp.utils

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
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
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showSnackbar(view: View, context: Context, msg: String): Snackbar {
        val snackbar = Snackbar.make(
            view,
            msg,
            Snackbar.LENGTH_SHORT
        )
        val view2 = snackbar.view
        val params = view2.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view2.layoutParams = params
        snackbar
            .setBackgroundTint(ContextCompat.getColor(context, R.color.almond))
            .setActionTextColor(ContextCompat.getColor(context, R.color.chestnut))
            .setTextColor(ContextCompat.getColor(context, R.color.smoky))
            .show()
        return snackbar
    }

    fun showSnackbarAction(
        view: View,
        context: Context,
        msg: String,
        fragment: Fragment,
        actionMsg: String
    ): Snackbar {
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        val view2 = snackbar.view
        val params = view2.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view2.layoutParams = params
        snackbar
            .setAction(actionMsg) {
                NavHostFragment.findNavController(fragment).popBackStack()
            }
            .setBackgroundTint(ContextCompat.getColor(context, R.color.almond))
            .setActionTextColor(ContextCompat.getColor(context, R.color.chestnut))
            .setTextColor(ContextCompat.getColor(context, R.color.smoky))
            .show()
        return snackbar
    }
}