package com.nathalie.coffeeapp.ui.drink

import android.graphics.BitmapFactory
import android.os.Bundle
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
import com.nathalie.coffeeapp.utils.Utils.showSnackbar
import com.nathalie.coffeeapp.utils.Utils.showSnackbarAction
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
                //if image is not null, decode using decodeByteArray
                //else if defaultImage is not null, decode using decodeResources
                //else if both are null, default image set in xml will be displayed
                if (it.image != null) {
                    it.image.let { bytes ->
                        val bitmap = BitmapFactory.decodeByteArray(it.image, 0, bytes.size)
                        ivDrinkImage.setImageBitmap(bitmap)
                    }
                } else if (it.defaultImage != null) {
                    val id = resources.getIdentifier(
                        it.defaultImage, "drawable",
                        context?.packageName
                    )
                    val img = BitmapFactory.decodeResource(resources, id)
                    ivDrinkImage.setImageBitmap(img)
                } else ivDrinkImage.setImageResource(R.drawable.mocha)

                tvTitle.text = it.title
                tvSubtitle.text = it.subtitle
                tvDetails.text = it.details
                tvIngredients.text = it.ingredients

                if (it.favorite == true) btnFav.setImageResource(R.drawable.ic_favorite)
                else btnFav.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        //when btnFav/heart shape icon is clicked, checked is drink.favorite is true
        //if true, make drink.favorite false, else make it true
        //snakcbar will pop up
        binding.btnFav.setOnClickListener { _ ->
            var favMsg = ""

            favMsg = if (viewModel.isFav()) {
                viewModel.favDrink(navArgs.id, false)
                "Removed from favorite!"
            } else {
                viewModel.favDrink(navArgs.id, true)
                "Added to favorite!"
            }

            showSnackbar(
                requireView(),
                requireContext(),
                favMsg
            )
        }


        //when btnEdit is clicked, take user to edit drink fragment
        binding.btnEdit.setOnClickListener {
            val action = DrinkDetailFragmentDirections.actionDrinkDetailToEdit(navArgs.id)
            NavHostFragment.findNavController(this).navigate(action)
        }

        // when delete btn is clicked, show a Dialog that requests confirmation
        // if confirmed, this drink is deleted
        binding.btnDelete.setOnClickListener {
            val title = binding.tvTitle.text.toString()
            val bundle = Bundle()
            bundle.putBoolean("refresh", true)
            MaterialAlertDialogBuilder(requireContext(), R.style.CoffeeApp_AlertDialog)
                .setTitle("Delete ${binding.tvTitle.text}?")
                .setCancelable(true)
                .setPositiveButton("Delete") { _, it ->
                    viewModel.deleteDrink(navArgs.id)
                    setFragmentResult("from_delete_drink", bundle)
                    NavHostFragment.findNavController(this).popBackStack()
                    showSnackbar(requireView(), requireContext(), "$title deleted!")
                }.setNegativeButton("Cancel") { _, it ->
                }
                .show()
        }

        // from edit drink fragment. After refresh, show Snackbar with an action btn to go back to Drink Fragment
        setFragmentResultListener("from_edit_drink") { _, result ->
            val refresh = result.getBoolean("refresh")
            val title = result.getString("title")
            parentViewModel.shouldRefreshDrinks(refresh)
            snackbar = showSnackbarAction(
                requireView(),
                requireContext(),
                "$title updated!",
                this,
                "Back to drinks"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbar?.dismiss()
    }

}