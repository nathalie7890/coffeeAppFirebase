package com.nathalie.coffeeapp.ui.presentation.bean

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.nathalie.coffeeapp.MyApplication
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.databinding.FragmentAddBeanBinding
import com.nathalie.coffeeapp.ui.utils.Utils
import com.nathalie.coffeeapp.ui.utils.Utils.showSnackbar
import com.nathalie.coffeeapp.viewmodels.bean.AddBeanViewModel

// Fragment/View bound to the AddBean UI
class AddBeanFragment : Fragment() {
    private lateinit var binding: FragmentAddBeanBinding
    private lateinit var filePickerLauncher: ActivityResultLauncher<String>
    private var bytes: ByteArray? = null

    // accessing the corresponding viewModel functions
    private val viewModel: AddBeanViewModel by viewModels {
        AddBeanViewModel.Provider((requireActivity().applicationContext as MyApplication).beanRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // defining the .xml file to bind this fragment to
        binding = FragmentAddBeanBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // when an image file is selected, convert it to byteArray and store it in variable bytes
        // decode the bytes to bitmap and display the image on ivBeanImage
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                bytes = inputStream?.readBytes()
                inputStream?.close()

                val bitmap = bytes?.let { it1 -> BitmapFactory.decodeByteArray(bytes, 0, it1.size) }
                binding.ivBeanImage.setImageBitmap(bitmap)
            }
        }

        binding.run {
            // when the this image image is clicked, opens gallery
            ivBeanImage.setOnClickListener {
                filePickerLauncher.launch("image/*")
            }

            // when addBtn is clicked, add bean to room db and go back to the previous fragment
            btnAdd.setOnClickListener {
                val title = binding.etTitle.text.toString()
                val subtitle = binding.etSubtitle.text.toString()
                val taste = binding.etTaste.text.toString()
                val details = binding.etDetails.text.toString()
                val body = binding.sliderBody.value.toInt()
                val aroma = binding.sliderAroma.value.toInt()
                val caffeine = binding.sliderCaffeine.value.toInt()

                //validate input
                if (validate(title, subtitle, taste, details)) {
                    val bean =
                        Bean(null, title, subtitle, taste, details, body, aroma, caffeine, bytes)
                    viewModel.addBean(bean)
                    val bundle = Bundle()
                    bundle.putBoolean("refresh", true)
                    setFragmentResult("from_add_bean", bundle)
                    NavHostFragment.findNavController(this@AddBeanFragment).popBackStack()
                    showSnackbar(requireView(), requireContext(), "$title added to Coffee Beans!")
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

    //check if every input contains value
    private fun validate(vararg list: String): Boolean {
        for (field in list) {
            if (field.isEmpty()) {
                return false
            }
        }
        return true
    }
}
