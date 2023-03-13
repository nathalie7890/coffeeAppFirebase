package com.nathalie.coffeeapp.ui.presentation.roast

import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast
import com.nathalie.coffeeapp.databinding.FragmentAddRoastBinding
import com.nathalie.coffeeapp.ui.presentation.BaseFragment

abstract class BaseRoastFragment : BaseFragment<FragmentAddRoastBinding>() {
    override fun getLayoutResource() = R.layout.fragment_add_roast

    fun getRoast(): Roast? {
        return binding?.run {
            val title = etTitle.text.toString()
            val details = etDetails.text.toString()

            Roast(null, title, details, "", "", "")
        }
    }
}