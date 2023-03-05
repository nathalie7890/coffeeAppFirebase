package com.nathalie.coffeeapp.ui.presentation.roast

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nathalie.coffeeapp.MyApplication
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.Roast
import com.nathalie.coffeeapp.databinding.FragmentEditRoastBinding
import com.nathalie.coffeeapp.ui.utils.Utils
import com.nathalie.coffeeapp.ui.utils.Utils.showSnackbar
import com.nathalie.coffeeapp.viewmodels.bean.EditBeanViewModel
import com.nathalie.coffeeapp.viewmodels.roast.EditRoastViewModel

class EditRoastFragment : Fragment() {
    private lateinit var binding: FragmentEditRoastBinding
    private lateinit var filePickerLauncher: ActivityResultLauncher<String>
    private var imageBytes: ByteArray? = null
    private lateinit var defaultImage: String
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
                //if image is not null, decode using decodeByteArray
                //else if defaultImage is not null, decode using decodeResources
                //else if both are null, default image set in xml will be displayed
                if (it.image != null) {
                    it.image.let { bytes ->
                        val bitmap = BitmapFactory.decodeByteArray(it.image, 0, bytes.size)
                        ivRoastImage.setImageBitmap(bitmap)
                    }
                } else if (it.defaultImage != null) {
                    val id = resources.getIdentifier(
                        it.defaultImage, "drawable",
                        context?.packageName
                    )
                    val img = BitmapFactory.decodeResource(resources, id)
                    ivRoastImage.setImageBitmap(img)
                } else ivRoastImage.setImageResource(R.drawable.upload_image_roast)

                etTitle.setText(it.title)
                etDetails.setText(it.details)
                defaultImage = it.defaultImage.toString()
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

                if (title.isNotEmpty() && details.isNotEmpty()) {
                    val roast = Roast(navArgs.id, title, details, imageBytes, defaultImage)
                    viewModel.editRoast(navArgs.id, roast)
                    val bundle = Bundle()
                    bundle.putBoolean("refresh", true)
                    setFragmentResult("from_edit_roast", bundle)
                    NavHostFragment.findNavController(this@EditRoastFragment).popBackStack()

                    showSnackbar(
                        requireView(),
                        requireContext(),
                        "$title updated!"
                    )
                } else {
                    showSnackbar(
                        requireView(),
                        requireContext(),
                        "Make sure you fill in everything!"
                    )
                }
            }

            //when delete btn is clicked, confirmation dialog will pop up
            //if confirmed, drink will be deleted and snackbar will indicate the users of the deletion
            btnDelete.setOnClickListener {
                val title = etTitle.text.toString()
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)

                MaterialAlertDialogBuilder(requireContext(), R.style.CoffeeApp_AlertDialog)
                    .setTitle("Delete $title?")
                    .setCancelable(true)
                    .setPositiveButton("Delete") { _, it ->
                        viewModel.deleteRoast(navArgs.id)
                        setFragmentResult("from_delete_drink", bundle)
                        NavHostFragment.findNavController(this@EditRoastFragment).popBackStack()
                        showSnackbar(requireView(), requireContext(), "$title deleted!")
                    }.setNegativeButton("Cancel") { _, it ->
                    }
                    .show()
            }
        }
    }
}