package com.nathalie.coffeeapp.ui.presentation.drink

import android.util.Log
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.databinding.FragmentAddDrinkBinding
import com.nathalie.coffeeapp.ui.presentation.BaseFragment

// Base fragment for Drinks to extend from
abstract class BaseDrinkFragment : BaseFragment<FragmentAddDrinkBinding>() {
    override fun getLayoutResource() = R.layout.fragment_add_drink

    // gets data from the EditText boxes and creates a Drink model
    fun getDrink(category: Int): Drink? {
        return binding?.run {
            val title = etTitle.text.toString()
            val subtitle = etSubtitle.text.toString()
            val details = etDetails.text.toString()
            val ingredients = etIngredients.text.toString()

            Drink(
                null, title, subtitle, details, ingredients, category, 2, ""
            )
        }
    }
}