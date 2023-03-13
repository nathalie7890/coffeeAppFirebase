package com.nathalie.coffeeapp.ui.presentation.roast

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.ui.utils.Utils.showSnackbar
import com.nathalie.coffeeapp.ui.viewmodels.roast.AddRoastViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddRoastFragment : BaseRoastFragment() {
    override val viewModel: AddRoastViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_add_roast

    //for selecting image from gallery
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private var fileUri: Uri? = null

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        //select image from gallery and display in ivDrinkImage
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            fileUri = it
            it?.let { uri ->
                binding?.ivRoastImage?.setImageURI(uri)
            }
        }

        binding?.run {
            ivRoastImage.setOnClickListener {
                imagePickerLauncher.launch("image/*")
            }
        }

        binding?.run {
            btnAdd.setOnClickListener {
                val title = etTitle.text.toString()
                val details = etDetails.text.toString()

                if (title.isNotEmpty() && details.isNotEmpty()) {
                    val roast = getRoast()
                    roast?.let {
                        viewModel.addRoast(it, fileUri)
                        val bundle = Bundle()
                        bundle.putBoolean("refresh", true)
                        setFragmentResult("from_add_roast", bundle)
                        navController.popBackStack()
                        showSnackbar(
                            requireView(),
                            requireContext(),
                            "$title added to Roast Levels"
                        )
                    }
                } else {
                    showSnackbar(
                        requireView(),
                        requireContext(),
                        "Make sure you fill in everything!"
                    )
                }
            }
        }
    }
}