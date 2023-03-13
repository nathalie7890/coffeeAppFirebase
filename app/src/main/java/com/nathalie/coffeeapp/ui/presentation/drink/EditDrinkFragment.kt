package com.nathalie.coffeeapp.ui.presentation.drink


import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.ui.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import com.nathalie.coffeeapp.ui.viewmodels.drink.EditDrinkViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditDrinkFragment : BaseDrinkFragment() {
    override val viewModel: EditDrinkViewModel by viewModels()

    //for selecting image from gallery
    private lateinit var filePickerLauncher: ActivityResultLauncher<String>
    private var imgBytes: ByteArray? = null

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        val navArgs: EditDrinkFragmentArgs by navArgs()
        viewModel.getDrinkById(navArgs.id)
        var category = 0

        //select image from gallery and display in ivDrinkImage
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                imgBytes = inputStream?.readBytes()
                inputStream?.close()

                val bitmap =
                    imgBytes?.let { it1 -> BitmapFactory.decodeByteArray(imgBytes, 0, it1.size) }
                binding?.ivDrinkImage?.setImageBitmap(bitmap)

            }
        }

        viewModel.drink.observe(viewLifecycleOwner) {
            binding?.run {
                etTitle.setText(it.title)
                etSubtitle.setText(it.subtitle)
                etDetails.setText(it.details)
                etIngredients.setText(it.ingredients)
                imgBytes = it.image.toByteArray()
                category = it.category
                if (category == 1) btnClassic.isChecked = true
                else btnCraft.isChecked = true
                btnAdd.text = "Save"

                drinkRadioGroup.setOnCheckedChangeListener { _, id ->
                    category = if (id == R.id.btnClassic) 1
                    else 2
                }

                //select image from gallery
                ivDrinkImage.setOnClickListener {
                    filePickerLauncher.launch("image/*")
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
                Utils.showSnackbar(requireView(), requireContext(), "Drink updated!")
            }
        }
    }

}