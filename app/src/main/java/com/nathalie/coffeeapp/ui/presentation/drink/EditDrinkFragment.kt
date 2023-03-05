package com.nathalie.coffeeapp.ui.presentation.drink


import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.nathalie.coffeeapp.R
import dagger.hilt.android.AndroidEntryPoint
import com.nathalie.coffeeapp.ui.viewmodels.drink.EditDrinkViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditDrinkFragment : BaseDrinkFragment() {
    override val viewModel: EditDrinkViewModel by viewModels()

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        val navArgs: EditDrinkFragmentArgs by navArgs()
        viewModel.getDrinkById(navArgs.id)
        var category = 0

        viewModel.drink.observe(viewLifecycleOwner) {
            binding?.run {
                etTitle.setText(it.title)
                etSubtitle.setText(it.subtitle)
                etDetails.setText(it.details)
                etIngredients.setText(it.ingredients)
                category = it.category
                if (category == 1) btnClassic.isChecked = true
                else btnCraft.isChecked = true
                btnAdd.text = "Save"

                drinkRadioGroup.setOnCheckedChangeListener { _, id ->
                    category = if (id == R.id.btnClassic) 1
                    else 2
                }

                btnAdd.setOnClickListener {
                    val drink = getDrink(category)
                    drink?.let {
                        viewModel.editDrink(navArgs.id, it)
                    }
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
                setFragmentResult("finish_edit_drink", bundle)
                navController.popBackStack()
            }
        }
    }

}