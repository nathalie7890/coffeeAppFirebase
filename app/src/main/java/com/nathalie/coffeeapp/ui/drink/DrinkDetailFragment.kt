package com.nathalie.coffeeapp.ui.drink

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nathalie.coffeeapp.MyApplication
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.databinding.FragmentDrinkDetailBinding
import com.nathalie.coffeeapp.viewmodels.drink.DrinkDetailViewModel
import com.nathalie.coffeeapp.viewmodels.MainViewModel

class DrinkDetailFragment : Fragment() {
    private lateinit var binding: FragmentDrinkDetailBinding
    val viewModel: DrinkDetailViewModel by viewModels {
        DrinkDetailViewModel.Provider((requireContext().applicationContext as MyApplication).drinkRepo)
    }
    private val parentViewModel: MainViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrinkDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navArgs: DrinkDetailFragmentArgs by navArgs()

        viewModel.getDrinkById(navArgs.id)
        viewModel.drink.observe(viewLifecycleOwner) {
            binding.run {
                it.image?.let { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(it.image, 0, bytes.size)
                    ivDrinkImage.setImageBitmap(bitmap)
                }
                tvTitle.text = it.title
                tvSubtitle.text = it.subtitle
                tvDetails.text = it.details
                tvIngredients.text = it.ingredients

                if (it.favorite == true) btnFav.setImageResource(R.drawable.ic_favorite)
                else btnFav.setImageResource(R.drawable.ic_favorite_border)

                //when favorite btn is clicked, toggle the hasFav value of drink and show a snackbar
                btnFav.setOnClickListener { view ->
                    var favMsg = ""
                    favMsg = if (it.favorite == false) {
                        viewModel.favDrink(navArgs.id, true)
                        btnFav.setImageResource(R.drawable.ic_favorite)
                        parentViewModel.shouldRefreshDrinks(true)
                        "Added to favorite!"
                    } else {
                        viewModel.favDrink(navArgs.id, false)
                        btnFav.setImageResource(R.drawable.ic_favorite_border)
                        parentViewModel.shouldRefreshDrinks(true)
                        "Removed from favorite!"
                    }
//                    parentViewModel.shouldRefreshDrinks(true)

                    Snackbar.make(requireView(), favMsg, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(resources.getColor(R.color.almond))
                        .setActionTextColor(resources.getColor(R.color.chestnut))
                        .setTextColor(resources.getColor(R.color.smoky))
                        .setAnchorView(binding.btnEdit)
                        .show()
                }
            }
        }


        binding.btnEdit.setOnClickListener {
            val action = DrinkDetailFragmentDirections.actionDrinkDetailToEdit(navArgs.id)
            NavHostFragment.findNavController(this).navigate(action)
        }

        //when delete btn is clicked, show a Dialog that requests confirmation
        //if confirmed, this drink is deleted
        binding.btnDelete.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("refresh", true)
            MaterialAlertDialogBuilder(requireContext(), R.style.CoffeeApp_AlertDialog)
                .setTitle("Delete ${binding.tvTitle.text}?")
                .setCancelable(true)
                .setPositiveButton("Delete") { _, it ->
                    viewModel.deleteDrink(navArgs.id)
                    setFragmentResult("from_delete_drink", bundle)
                    NavHostFragment.findNavController(this).popBackStack()
                }.setNegativeButton("Cancel") { _, it ->
                }
                .show()
        }

        //from edit drink fragment. After refresh, show Snackbar with an action btn to go back to Drink Fragment
        setFragmentResultListener("from_edit_drink") { _, result ->
            val refresh = result.getBoolean("refresh")
            parentViewModel.shouldRefreshDrinks(refresh)
            snackbar = Snackbar.make(requireView(), "Drink updated!", Snackbar.LENGTH_LONG)
            snackbar!!
                .setAction("Back To Drinks") {
                    NavHostFragment.findNavController(this).popBackStack()
                }
                .setBackgroundTint(resources.getColor(R.color.almond))
                .setActionTextColor(resources.getColor(R.color.chestnut))
                .setTextColor(resources.getColor(R.color.smoky))
                .setAnchorView(binding.btnEdit)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbar?.dismiss()
    }

}