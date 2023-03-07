package com.nathalie.coffeeapp.ui.presentation.bean

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.nathalie.coffeeapp.ui.utils.Utils
import com.nathalie.coffeeapp.ui.viewmodels.bean.EditBeanViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditBeanFragment : BaseBeanFragment() {
    override val viewModel: EditBeanViewModel by viewModels()

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        val navArgs: EditBeanFragmentArgs by navArgs()
        var img: String

        viewModel.getBeanById(navArgs.id)

        viewModel.bean.observe(viewLifecycleOwner) {
            binding?.run {
                etTitle.setText(it.title)
                etSubtitle.setText(it.subtitle)
                etTaste.setText(it.taste)
                etDetails.setText(it.details)
                sliderBody.value = it.body.toFloat()
                sliderAroma.value = it.aroma.toFloat()
                sliderCaffeine.value = it.caffeine.toFloat()

                btnAdd.text = "Save"
                btnAdd.setOnClickListener {
                    val bean = getBean("")
                    bean?.let {
                        viewModel.editBean(navArgs.id, it)
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