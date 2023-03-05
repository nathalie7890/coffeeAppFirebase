package com.nathalie.coffeeapp.ui.presentation.drink


import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nathalie.coffeeapp.R
import  com.nathalie.coffeeapp.ui.viewmodels.drink.AddDrinkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddDrinkFragment : BaseDrinkFragment() {
    override val viewModel: AddDrinkViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_add_drink

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        var category = 1

        binding?.run {
            drinkRadioGroup.setOnCheckedChangeListener { _, id ->
                category = if (id == R.id.btnClassic) 1
                else 2
            }

            btnAdd.setOnClickListener {
                val drink = getDrink(category)
                drink?.let {
                    viewModel.addDrink(it)
                }
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            viewModel.finish.collect {
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("finish_add_drink", bundle)
                navController.popBackStack()
            }
        }
    }
}