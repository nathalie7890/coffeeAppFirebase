package com.nathalie.coffeeapp.ui.presentation.roast

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.ui.utils.Utils
import com.nathalie.coffeeapp.ui.viewmodels.roast.EditRoastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditRoastFragment : BaseRoastFragment() {
    override val viewModel: EditRoastViewModel by viewModels()

    //for selecting image from gallery
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private var fileUri: Uri? = null

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        val navArgs: EditRoastFragmentArgs by navArgs()

        //select image from gallery and display in ivDrinkImage
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            fileUri = it
            it?.let { uri ->
                binding?.ivRoastImage?.setImageURI(uri)
            }
        }
        viewModel.getRoastById(navArgs.id)

        viewModel.roast.observe(viewLifecycleOwner) { roast ->
            binding?.run {
                etTitle.setText(roast.title)
                etDetails.setText(roast.details)
                btnAdd.text = "Save"

                ivRoastImage.setOnClickListener {
                    imagePickerLauncher.launch("image/*")
                }

                btnAdd.setOnClickListener {
                    val newRoast =
                        getRoast()?.copy(image = roast.image, uid = roast.uid)
                    newRoast?.let {
                        viewModel.editRoast(navArgs.id, it, fileUri)
                    }
                }

                btnDelete.setOnClickListener {
                    val title = etTitle.text.toString()
                    val bundle = Bundle()
                    bundle.putBoolean("refresh", true)

                    MaterialAlertDialogBuilder(
                        requireContext(),
                        R.style.CoffeeApp_AlertDialog
                    ).setTitle("Delete $title?")
                        .setCancelable(true)
                        .setPositiveButton("Delete") { _, _ ->
                            viewModel.deleteRoast(navArgs.id)
                            Utils.showSnackbar(requireView(), requireContext(), "$title deleted!")
                        }.setNegativeButton("Cancel") { _, _ ->
                        }.show()
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
                setFragmentResult("from_edit_roast", bundle)
                navController.popBackStack()
                Utils.showSnackbar(requireView(), requireContext(), "Coffee bean updated!")
            }
        }
    }
}