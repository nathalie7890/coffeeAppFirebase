package com.nathalie.coffeeapp.ui.presentation.bean

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.ui.utils.Utils
import com.nathalie.coffeeapp.ui.viewmodels.bean.EditBeanViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditBeanFragment : BaseBeanFragment() {
    override val viewModel: EditBeanViewModel by viewModels()

    //for selecting image from gallery
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private var fileUri: Uri? = null

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        val navArgs: EditBeanFragmentArgs by navArgs()

        //select image from gallery and display in ivDrinkImage
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            fileUri = it
            it?.let { uri ->
                binding?.ivBeanImage?.setImageURI(uri)
            }
        }


        viewModel.getBeanById(navArgs.id)

        viewModel.bean.observe(viewLifecycleOwner) { bean ->
            binding?.run {
                etTitle.setText(bean.title)
                etSubtitle.setText(bean.subtitle)
                etTaste.setText(bean.taste)
                etDetails.setText(bean.details)
                sliderBody.value = bean.body.toFloat()
                sliderAroma.value = bean.aroma.toFloat()
                sliderCaffeine.value = bean.caffeine.toFloat()

                bean.image?.let {
                    StorageService.getImageUri(it) { uri ->
                        Glide.with(this@EditBeanFragment)
                            .load(uri)
                            .placeholder(R.color.chocolate)
                            .into(ivBeanImage)
                    }
                }

                ivBeanImage.setOnClickListener {
                    imagePickerLauncher.launch("image/*")
                }

                btnAdd.text = "Save"
                btnAdd.setOnClickListener {
                    val newBean =
                        getBean()?.copy(image = bean.image, uid = bean.uid)
                    newBean?.let {
                        viewModel.editBean(navArgs.id, it, fileUri)
                    }
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
                setFragmentResult("finish_edit_bean", bundle)
                navController.popBackStack()
                Utils.showSnackbar(requireView(), requireContext(), "Coffee bean updated!")
            }
        }

    }
}