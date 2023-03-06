package com.nathalie.coffeeapp.ui.presentation.drink


import android.os.Bundle
import android.util.Log
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
                if (it.favorite == 2) {
                    btnFav.setImageResource(R.drawable.ic_favorite_border)
                } else {
                    btnFav.setImageResource(R.drawable.ic_favorite)
                }

                btnDelete.setOnClickListener { _ ->
                    viewModel.deleteDrink(it.id.toString())
                }

                btnEdit.setOnClickListener {
                    val action = DrinkDetailFragmentDirections.actionDrinkDetailToEdit(navArgs.id)
                    navController.navigate(action)
                }

                btnFav.setOnClickListener { _ ->
                    if (viewModel.isFav() == 2) {
                        viewModel.favDrink(navArgs.id, 1)
                    } else {
                        viewModel.favDrink(navArgs.id, 2)
                    }
                }
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        val navArgs: DrinkDetailFragmentArgs by navArgs()

        lifecycleScope.launch {
            viewModel.finish.collect {
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("finish_delete_drink", bundle)
                navController.popBackStack()
            }
        }

        lifecycleScope.launch {
            viewModel.btnFavClicked.collect {
                viewModel.onRefresh(navArgs.id)
            }
        }
    }
}