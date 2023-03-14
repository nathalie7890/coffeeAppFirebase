package com.nathalie.coffeeapp.ui.utils

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.nathalie.coffeeapp.R

// Object that contains various utility functions(used in DrinksFragment)
object Utils {
    fun <T> RecyclerView.Adapter<*>.update(
        oldList: List<T>,
        newList: List<T>,
        compare: (T, T) -> Boolean
    ) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(oldList[oldItemPosition], newList[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        })

        diff.dispatchUpdatesTo(this)
    }

    // Checks if the edit text is empty
    fun validate(vararg fields: String): Boolean {
        fields.forEach { field ->
            if (field.isBlank()) {
                return false
            }
        }

        return true
    }

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

    // for other files to use this custom build snackbar
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