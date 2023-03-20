package com.nathalie.coffeeapp.ui.presentation.drink


import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.ui.utils.Utils
import  com.nathalie.coffeeapp.ui.viewmodels.drink.AddDrinkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
// Fragment/View bound to the AddDrink UI
class AddDrinkFragment : BaseDrinkFragment() {
    override val viewModel: AddDrinkViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_add_drink

    //for selecting image from gallery
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private var fileUri: Uri? = null


    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        var category = 0

        //select image from gallery and display in ivDrinkImage
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            fileUri = it
            it?.let { uri ->
                binding?.ivDrinkImage?.setImageURI(uri)
            }
        }

        binding?.run {
            //select drink category
            drinkRadioGroup.setOnCheckedChangeListener { _, id ->
                category = if (id == R.id.btnClassic) 1
                else 2
            }

            //select image from gallery
            ivDrinkImage.setOnClickListener {
                imagePickerLauncher.launch("image/*")
            }

            //add drink button
            btnAdd.setOnClickListener {
                val drink = getDrink()
                drink?.let {
                    viewModel.addDrink(it, category, fileUri)
                }
            }

        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            // if finish is emitted from the viewmodel function, it will run the code below
            viewModel.finish.collect {
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("finish_add_drink", bundle)
                navController.popBackStack()
                Utils.showSnackbar(requireView(), requireContext(), "Drink added!")
            }
        }
    }
}