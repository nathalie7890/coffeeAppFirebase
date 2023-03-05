package com.nathalie.coffeeapp.ui.presentation.drink


import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.databinding.FragmentDrinkDetailBinding
import com.nathalie.coffeeapp.ui.presentation.BaseFragment
import  com.nathalie.coffeeapp.ui.viewmodels.drink.DrinkDetailViewModel
import com.nathalie.coffeeapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DrinkDetailFragment : BaseFragment<FragmentDrinkDetailBinding>() {
    override val viewModel: DrinkDetailViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_drink_detail

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        val navArgs: DrinkDetailFragmentArgs by navArgs()
        viewModel.getDrinkById(navArgs.id)

        viewModel.drink.observe(viewLifecycleOwner) {
            binding?.run {
                tvTitle.text = it.title
                tvSubtitle.text = it.subtitle
                tvDetails.text = it.details
                tvIngredients.text = it.ingredients

                btnDelete.setOnClickListener { _ ->
                    viewModel.deleteDrink(it.id.toString())
                }

                btnEdit.setOnClickListener {
                    val action = DrinkDetailFragmentDirections.actionDrinkDetailToEdit(navArgs.id)
                    navController.navigate(action)
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
                setFragmentResult("finish_delete_drink", bundle)
                navController.popBackStack()
            }
        }
    }

    companion object {
        private var drinkDetailFragmentInstance: DrinkDetailFragment? = null
        fun getInstance(): DrinkDetailFragment {
            if (drinkDetailFragmentInstance == null) {
                drinkDetailFragmentInstance = DrinkDetailFragment()
            }

            return drinkDetailFragmentInstance!!
        }
    }
}