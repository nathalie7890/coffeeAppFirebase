package com.nathalie.coffeeapp.ui.presentation.bean


import android.os.Bundle
import android.view.View
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
    override fun getLayoutResource() = R.layout.fragment_add_bean

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        binding?.run {
            btnAdd.setOnClickListener {
                val bean = getBean("")
                bean?.let {
                    viewModel.addBean(it)
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
                setFragmentResult("finish_add_bean", bundle)
                navController.popBackStack()
                Utils.showSnackbar(requireView(), requireContext(), "Coffee bean added!")
            }
        }
    }
}
