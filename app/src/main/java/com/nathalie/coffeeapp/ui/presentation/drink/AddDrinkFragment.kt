package com.nathalie.coffeeapp.ui.presentation.drink


import android.graphics.BitmapFactory
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
class AddDrinkFragment : BaseDrinkFragment() {
    override val viewModel: AddDrinkViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_add_drink

    //for selecting image from gallery
    private lateinit var filePickerLauncher: ActivityResultLauncher<String>
    private var bytes: ByteArray? = null


    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        var category = 1

        //select image from gallery and display in ivDrinkImage
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                bytes = inputStream?.readBytes()
                inputStream?.close()

                val bitmap = bytes?.let { it1 -> BitmapFactory.decodeByteArray(bytes, 0, it1.size) }
                binding?.ivDrinkImage?.setImageBitmap(bitmap)
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
                filePickerLauncher.launch("image/*")
            }

            //add drink button
            btnAdd.setOnClickListener {
                val drink = getDrink(category, bytes.toString())
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
                Utils.showSnackbar(requireView(), requireContext(), "Drink added!")
            }
        }
    }
}