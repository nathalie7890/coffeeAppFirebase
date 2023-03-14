package com.nathalie.coffeeapp.ui.presentation.roast

import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast
import com.nathalie.coffeeapp.databinding.FragmentAddRoastBinding
import com.nathalie.coffeeapp.ui.presentation.BaseFragment

// Base fragment for Drinks to extend from
abstract class BaseRoastFragment : BaseFragment<FragmentAddRoastBinding>() {
    override fun getLayoutResource() = R.layout.fragment_add_roast

    // gets data from the EditText boxes and creates a Roast model
    fun getRoast(): Roast? {
        return binding?.run {
            val title = etTitle.text.toString()
            val details = etDetails.text.toString()

            Roast(null, title, details, "")
        }
    }
}