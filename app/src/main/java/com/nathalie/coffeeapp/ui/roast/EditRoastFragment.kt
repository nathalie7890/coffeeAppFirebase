package com.nathalie.coffeeapp.ui.roast

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.nathalie.coffeeapp.MyApplication
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.Roast
import com.nathalie.coffeeapp.databinding.FragmentEditRoastBinding
import com.nathalie.coffeeapp.viewmodels.bean.EditBeanViewModel
import com.nathalie.coffeeapp.viewmodels.roast.EditRoastViewModel

class EditRoastFragment : Fragment() {
    private lateinit var binding: FragmentEditRoastBinding
    private lateinit var filePickerLauncher: ActivityResultLauncher<String>
    private var imageBytes: ByteArray? = null
    val viewModel: EditRoastViewModel by viewModels {
        EditRoastViewModel.Provider((requireActivity().applicationContext as MyApplication).roastRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRoastBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navArgs: EditRoastFragmentArgs by navArgs()

        viewModel.getRoastById(navArgs.id)
        viewModel.roast.observe(viewLifecycleOwner) {
            binding.run {
                it.image?.let { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(it.image, 0, bytes.size)
                    ivRoastImage.setImageBitmap(bitmap)
                }
                etTitle.setText(it.title)
                etDetails.setText(it.details)
            }
        }

        // when an image file is selected, convert it to byteArray and store it in variable imageBytes
        // decode imageBytes to bitmap and display the image on ivRoastImage
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                imageBytes = inputStream?.readBytes()
                inputStream?.close()

                val bitmap = imageBytes?.let { it1 ->
                    BitmapFactory.decodeByteArray(
                        imageBytes,
                        0,
                        it1.size
                    )
                }
                binding.ivRoastImage.setImageBitmap(bitmap)
            }
        }
        binding.run {
            // when the this image image is clicked, opens gallery
            ivRoastImage.setOnClickListener {
                filePickerLauncher.launch("image/*")
            }

            // when save btn is clicked, update roast and go back to previous fragment
            // a snackbar will pop up
            btnSave.setOnClickListener {
                val title = etTitle.text.toString()
                val details = etDetails.text.toString()

                val roast = Roast(navArgs.id, title, details, imageBytes)
                viewModel.editRoast(navArgs.id, roast)
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("from_edit_roast", bundle)
                NavHostFragment.findNavController(this@EditRoastFragment).popBackStack()

                val snackbar =
                    Snackbar.make(requireView(), "Roast level updated!", Snackbar.LENGTH_SHORT)
                val view2 = snackbar.view
                val params = view2.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP
                view2.layoutParams = params
                snackbar
                    .setBackgroundTint(resources.getColor(R.color.almond))
                    .setActionTextColor(resources.getColor(R.color.chestnut))
                    .setTextColor(resources.getColor(R.color.smoky))
                    .show()
            }

            btnDelete.setOnClickListener {
                viewModel.deleteRoast(navArgs.id)
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("from_delete_roast", bundle)
                NavHostFragment.findNavController(this@EditRoastFragment).popBackStack()

                val snackbar =
                    Snackbar.make(requireView(), "Roast deleted!", Snackbar.LENGTH_SHORT)
                val view2 = snackbar.view
                val params = view2.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP
                view2.layoutParams = params
                snackbar
                    .setBackgroundTint(resources.getColor(R.color.almond))
                    .setActionTextColor(resources.getColor(R.color.chestnut))
                    .setTextColor(resources.getColor(R.color.smoky))
                    .show()
            }
        }
    }
}