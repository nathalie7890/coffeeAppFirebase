package com.nathalie.coffeeapp.ui.presentation.drink


import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.ui.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import com.nathalie.coffeeapp.ui.viewmodels.drink.EditDrinkViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
// fragment bound to the Edit Drink UI
class EditDrinkFragment : BaseDrinkFragment() {
    override val viewModel: EditDrinkViewModel by viewModels()

    //for selecting image from gallery
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private var fileUri: Uri? = null

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        val navArgs: EditDrinkFragmentArgs by navArgs()

        // fetch single drink
        viewModel.getDrinkById(navArgs.id)
        var category = 0

        //select image from gallery and display in ivDrinkImage
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            fileUri = it
            it?.let { uri ->
                binding?.ivDrinkImage?.setImageURI(uri)
            }
        }

        viewModel.drink.observe(viewLifecycleOwner) { drink ->
            binding?.run {
                // binds the item data to display
                etTitle.setText(drink.title)
                etSubtitle.setText(drink.subtitle)
                etDetails.setText(drink.details)
                etIngredients.setText(drink.ingredients)
                category = drink.category

                //check radio button according to drink's category
                if (category == 1) btnClassic.isChecked = true
                else btnCraft.isChecked = true

                // displays image
                drink.image?.let {
                    StorageService.getImageUri(it) { uri ->
                        Glide.with(this@EditDrinkFragment)
                            .load(uri)
                            .placeholder(R.color.chocolate)
                            .into(ivDrinkImage)
                    }
                }

                btnAdd.text = "Save"

                // changes the value when clicked
                drinkRadioGroup.setOnCheckedChangeListener { _, id ->
                    category = if (id == R.id.btnClassic) 1
                    else 2
                }

                //select image from gallery
                ivDrinkImage.setOnClickListener {
                    imagePickerLauncher.launch("image/*")
                }

                // edits the drink
                btnAdd.setOnClickListener {
                    val newDrink = getDrink()?.copy(
                        category = category,
                        image = drink.image, editable = true, uid = drink.uid
                    )
                    newDrink?.let {
                        viewModel.editDrink(navArgs.id, it, fileUri)
                    }
                }
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            // when successfully edited, run the code below
            viewModel.finish.collect {
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("finish_edit_drink", bundle)
                navController.popBackStack()
                Utils.showSnackbar(requireView(), requireContext(), "Drink updated!")
            }
        }
    }

}