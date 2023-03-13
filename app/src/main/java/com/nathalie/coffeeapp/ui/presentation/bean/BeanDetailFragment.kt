package com.nathalie.coffeeapp.ui.presentation.bean

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.service.StorageService
import com.nathalie.coffeeapp.databinding.FragmentBeanDetailBinding
import com.nathalie.coffeeapp.ui.presentation.BaseFragment
import com.nathalie.coffeeapp.ui.utils.Utils
import com.nathalie.coffeeapp.ui.viewmodels.bean.BeanDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BeanDetailFragment : BaseFragment<FragmentBeanDetailBinding>() {
    override val viewModel: BeanDetailViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_bean_detail


    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        val navArgs: BeanDetailFragmentArgs by navArgs()
        viewModel.getBeanById(navArgs.id)

        viewModel.bean.observe(viewLifecycleOwner) {
            binding?.run {
                // setting text values of title, subtitle, taste and details
                tvTitle.text = it.title
                tvSubtitle.text = it.subtitle
                tvTaste.text = it.taste
                tvDetails.text = it.details

                // setting 3 sliders value according to the value of it.body, it.aroma and it.caffeine
                sliderBody.value = it.body.toFloat()
                sliderAroma.value = it.aroma.toFloat()
                sliderCaffeine.value = it.caffeine.toFloat()

                // disable sliding of all 3 sliders
                sliderBody.isEnabled = false
                sliderAroma.isEnabled = false
                sliderCaffeine.isEnabled = false

                it.image?.let {
                    StorageService.getImageUri(it) { uri ->
                        Glide.with(this@BeanDetailFragment)
                            .load(uri)
                            .placeholder(R.color.chocolate)
                            .into(ivBeanImage)
                    }
                }

                btnEdit.setOnClickListener {
                    val action = BeanDetailFragmentDirections.actionBeanDetailToEditBean(navArgs.id)
                    navController.navigate(action)
                }

                btnDelete.setOnClickListener { _ ->
                    MaterialAlertDialogBuilder(requireContext(), R.style.CoffeeApp_AlertDialog)
                        .setTitle("Delete ${binding!!.tvTitle.text}?")
                        .setCancelable(true)
                        .setPositiveButton("Delete") { _, _ ->
                            viewModel.deleteBean(it.id.toString())

                        }.setNegativeButton("Cancel") { _, _ ->
                        }
                        .show()
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
                setFragmentResult("finish_delete_bean", bundle)
                navController.popBackStack()
                Utils.showSnackbar(requireView(), requireContext(), "Coffee bean deleted!")
            }
        }
    }

}