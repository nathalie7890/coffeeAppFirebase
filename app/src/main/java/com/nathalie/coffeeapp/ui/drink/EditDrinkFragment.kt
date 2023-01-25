package com.nathalie.coffeeapp.ui.drink

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.nathalie.coffeeapp.MyApplication
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.Drink
import com.nathalie.coffeeapp.databinding.FragmentEditDrinkBinding
import com.nathalie.coffeeapp.viewmodels.drink.EditDrinkViewModel

class EditDrinkFragment : Fragment() {
    private lateinit var binding: FragmentEditDrinkBinding
    val viewModel: EditDrinkViewModel by viewModels {
        EditDrinkViewModel.Provider((requireActivity().applicationContext as MyApplication).drinkRepo)
    }
    private lateinit var filePickerLauncher: ActivityResultLauncher<String>
    private var imageBytes: ByteArray? = null
    private var category = 0
    private var isFav = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditDrinkBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navArgs: EditDrinkFragmentArgs by navArgs()

        viewModel.getDrinkById(navArgs.id)
        viewModel.drink.observe(viewLifecycleOwner) {
            binding.run {
                it.image?.let { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(it.image, 0, bytes.size)
                    ivDrinkImage.setImageBitmap(bitmap)
                }
                etTitle.setText(it.title)
                etSubtitle.setText(it.subtitle)
                etDetails.setText(it.details)
                etIngredients.setText(it.ingredients)
                category = it.category
                imageBytes = it.image
                isFav = it.favorite!!

                if (it.category == 1) btnClassic.isChecked = true
                else btnNonClassic.isChecked = true
            }
        }

        filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                imageBytes = inputStream?.readBytes()
                inputStream?.close()

                val bitmap = imageBytes?.let { it1 -> BitmapFactory.decodeByteArray(imageBytes, 0, it1.size) }
                binding.ivDrinkImage.setImageBitmap(bitmap)
            }
        }

        binding.ivDrinkImage.setOnClickListener {
            filePickerLauncher.launch("image/*")
        }

        binding.drinkRadioGroup.setOnCheckedChangeListener { _, id ->
            category = if (id == R.id.btnClassic) 1
            else 2
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val subtitle = binding.etSubtitle.text.toString()
            val details = binding.etDetails.text.toString()
            val ingredients = binding.etIngredients.text.toString()

            val drink =  Drink(navArgs.id, title, subtitle, details, ingredients, category, imageBytes, isFav)
            viewModel.editDrink(navArgs.id,drink)
            val bundle = Bundle()
            bundle.putBoolean("refresh", true)
            setFragmentResult("from_edit_drink", bundle)
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