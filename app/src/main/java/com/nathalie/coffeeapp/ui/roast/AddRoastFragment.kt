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
import com.google.android.material.snackbar.Snackbar
import com.nathalie.coffeeapp.MyApplication
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.Roast
import com.nathalie.coffeeapp.databinding.FragmentAddRoastBinding
import com.nathalie.coffeeapp.utils.Utils
import com.nathalie.coffeeapp.utils.Utils.showSnackbar
import com.nathalie.coffeeapp.viewmodels.roast.AddRoastViewModel

class AddRoastFragment : Fragment() {
    private lateinit var binding: FragmentAddRoastBinding
    private lateinit var filePickerLauncher: ActivityResultLauncher<String>
    private var bytes: ByteArray? = null
    private val viewModel: AddRoastViewModel by viewModels {
        AddRoastViewModel.Provider((requireActivity().applicationContext as MyApplication).roastRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRoastBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // when an image file is selected, convert it to byteArray and store it in variable bytes
        // decode the bytes to bitmap and display the image on ivRoastImage
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                bytes = inputStream?.readBytes()
                inputStream?.close()

                val bitmap = bytes?.let { it1 -> BitmapFactory.decodeByteArray(bytes, 0, it1.size) }
                binding.ivRoastImage.setImageBitmap(bitmap)
            }
        }

        binding.run {

            // when the this image image is clicked, opens gallery
            ivRoastImage.setOnClickListener {
                filePickerLauncher.launch("image/*")
            }

            // when add btn is clicked, add roast to room db and go back to the previous fragment
            btnAdd.setOnClickListener {
                val title = etTitle.text.toString()
                val details = etDetails.text.toString()

                //if title or details is empty, show a snackbar to remind user to fill in both inputs
                //else call viewModel.addRoast()
                if (title.isNotEmpty() && details.isNotEmpty()) {
                    val roast = Roast(null, title, details, bytes)
                    viewModel.addRoast(roast)
                    val bundle = Bundle()
                    bundle.putBoolean("refresh", true)
                    setFragmentResult("from_add_roast", bundle)
                    NavHostFragment.findNavController(this@AddRoastFragment).popBackStack()
                    showSnackbar(requireView(), requireContext(), "$title added to Roast Levels!")
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