package com.nathalie.coffeeapp.ui.bean

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
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.databinding.FragmentEditBeanBinding
import com.nathalie.coffeeapp.viewmodels.bean.EditBeanViewModel

class EditBeanFragment : Fragment() {
    private lateinit var binding: FragmentEditBeanBinding
    val viewModel: EditBeanViewModel by viewModels {
        EditBeanViewModel.Provider((requireActivity().applicationContext as MyApplication).beanRepo)
    }
    private lateinit var filePickerLauncher: ActivityResultLauncher<String>
    private var imageBytes: ByteArray? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBeanBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navArgs: EditBeanFragmentArgs by navArgs()

        viewModel.getBeanById(navArgs.id)
        viewModel.bean.observe(viewLifecycleOwner) {
            binding.run {
                it.image?.let { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(it.image, 0, bytes.size)
                    ivBeanImage.setImageBitmap(bitmap)
                }
                etTitle.setText(it.title)
                etSubtitle.setText(it.subtitle)
                etTaste.setText(it.taste)
                etDetails.setText(it.details)
                imageBytes = it.image
            }
        }

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
                binding.ivBeanImage.setImageBitmap(bitmap)
            }
        }

        binding.ivBeanImage.setOnClickListener {
            filePickerLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val subtitle = binding.etSubtitle.text.toString()
            val taste = binding.etTaste.text.toString()
            val details = binding.etDetails.text.toString()
            val body = binding.sliderBody.value.toInt()
            val aroma = binding.sliderAroma.value.toInt()
            val caffeine = binding.sliderCaffeine.value.toInt()

            val bean =
                Bean(navArgs.id, title, subtitle, taste, details, body, aroma, caffeine, imageBytes)
            viewModel.editBean(navArgs.id, bean)
            val bundle = Bundle()
            bundle.putBoolean("refresh", true)
            setFragmentResult("from_edit_bean", bundle)
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