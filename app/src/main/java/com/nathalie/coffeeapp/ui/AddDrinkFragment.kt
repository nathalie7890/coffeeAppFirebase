package com.nathalie.coffeeapp.ui

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.nathalie.coffeeapp.MyApplication
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.Model.Drink
import com.nathalie.coffeeapp.databinding.FragmentAddDrinkBinding
import com.nathalie.coffeeapp.viewmodels.AddDrinkViewModel

class AddDrinkFragment : Fragment() {
    private lateinit var filePickerLauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentAddDrinkBinding
    private var bytes: ByteArray? = null
    private val viewModel: AddDrinkViewModel by viewModels {
        AddDrinkViewModel.Provider((requireActivity().applicationContext as MyApplication).drinkRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDrinkBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                binding.tvImageName.text = requireContext().contentResolver.getFileName(uri)

                val inputStream = requireContext().contentResolver.openInputStream(uri)
                bytes = inputStream?.readBytes()
                inputStream?.close()

                val bitmap = bytes?.let { it1 -> BitmapFactory.decodeByteArray(bytes, 0, it1.size) }
                binding.ivDrinkImage.setImageBitmap(bitmap)
            }
        }

        binding.ivDrinkImage.setOnClickListener {
            filePickerLauncher.launch("image/*")
        }

        binding.btnAdd.setOnClickListener {
            var category: String = ""
            val title = binding.etTitle.text.toString()
            val subtitle = binding.etSubtitle.text.toString()
            val details = binding.etDetails.text.toString()

            binding.drinkRadioGroup.setOnCheckedChangeListener { _, id ->
                if (id == R.id.btnHot) category = "Hot"
                else category = "Cold"
            }

            viewModel.addDrink(Drink(null,title, subtitle, details, category))
            val bundle = Bundle()
            bundle.putBoolean("refresh", true)
            setFragmentResult("from_add_item", bundle)
            NavHostFragment.findNavController(this).popBackStack()
        }
    }

    private fun ContentResolver.getFileName(fileUri: Uri): String {
        val cursor = this.query(fileUri, null, null, null, null)

        cursor?.let {
            val name = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            return cursor.getString(name)
        }
        cursor?.close()

        return "Unknown"
    }
}