package com.nathalie.coffeeapp.ui.presentation.bean


import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.ui.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import com.nathalie.coffeeapp.ui.viewmodels.bean.AddBeanViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
// Fragment/View bound to the AddBean UI
class AddBeanFragment : BaseBeanFragment() {
    override val viewModel: AddBeanViewModel by viewModels()

    //for selecting image from gallery
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private var fileUri: Uri? = null
    override fun getLayoutResource() = R.layout.fragment_add_bean

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        //select image from gallery and display in ivDrinkImage
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            fileUri = it
            it?.let { uri ->
                binding?.ivBeanImage?.setImageURI(uri)
            }
        }

        binding?.run {
            btnAdd.setOnClickListener {
                val bean = getBean()
                bean?.let {
                    viewModel.addBean(it, fileUri)
                }
            }

            ivBeanImage.setOnClickListener {
                imagePickerLauncher.launch("image/*")
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            viewModel.finish.collect {
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("finish_add_bean", bundle)
                navController.popBackStack()
                Utils.showSnackbar(requireView(), requireContext(), "Coffee bean added!")
            }
        }
    }
}
