package com.nathalie.coffeeapp.ui.presentation.drink


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.databinding.FragmentDrinkDetailBinding
import com.nathalie.coffeeapp.ui.presentation.BaseFragment
import com.nathalie.coffeeapp.ui.utils.Utils
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

                //if drink's uid is default, hide the edit btn
                if (it.uid == "default") btnEdit.visibility = View.INVISIBLE


                it.image?.let {
                    StorageService.getImageUri(it) { uri ->
                        Glide.with(this@DrinkDetailFragment)
                            .load(uri)
                            .placeholder(R.color.chocolate)
                            .into(ivDrinkImage)
                    }
                }

                //delete drink
                btnDelete.setOnClickListener { _ ->
                    MaterialAlertDialogBuilder(requireContext(), R.style.CoffeeApp_AlertDialog)
                        .setTitle("Delete ${binding!!.tvTitle.text}?")
                        .setCancelable(true)
                        .setPositiveButton("Delete") { _, _ ->
                            viewModel.deleteDrink(it.id.toString())

                        }.setNegativeButton("Cancel") { _, _ ->
                        }
                        .show()
                }

                //goes to edit drink fragment
                btnEdit.setOnClickListener {
                    val action = DrinkDetailFragmentDirections.actionDrinkDetailToEdit(navArgs.id)
                    navController.navigate(action)
                }

                //update drink.favorite 1 = false, 2 = true
                btnFav.setOnClickListener { _ ->
                    var msg: String
                    msg = if (viewModel.isFav() == 2) {
                        viewModel.favDrink(navArgs.id, 1)
                        "Added drink to favorite!"
                    } else {
                        viewModel.favDrink(navArgs.id, 2)
                        "Removed drink from favorite!"
                    }

                    Utils.showSnackbar(
                        requireView(),
                        requireContext(),
                        msg
                    )
                }
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        val navArgs: DrinkDetailFragmentArgs by navArgs()

        //after delete drink, set fragment result, goes back to previous fragment and show snackbar
        lifecycleScope.launch {
            viewModel.finish.collect {
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("finish_delete_drink", bundle)
                navController.popBackStack()
                Utils.showSnackbar(requireView(), requireContext(), "Drink deleted!")
            }
        }

        //after updating drink.favorite, refresh the drink
        lifecycleScope.launch {
            viewModel.btnFavClicked.collect {
                viewModel.onRefresh(navArgs.id)
            }
        }
    }
}