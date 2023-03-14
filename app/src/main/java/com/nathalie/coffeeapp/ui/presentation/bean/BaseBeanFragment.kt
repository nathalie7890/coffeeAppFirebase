package com.nathalie.coffeeapp.ui.presentation.bean

import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.databinding.FragmentAddBeanBinding
import com.nathalie.coffeeapp.ui.presentation.BaseFragment

// Base fragment for Beans to extend from
abstract class BaseBeanFragment : BaseFragment<FragmentAddBeanBinding>() {
    override fun getLayoutResource() = R.layout.fragment_add_bean

    // gets data from the EditText boxes and creates a Bean model
    fun getBean(): Bean? {
        return binding?.run {
            val title = etTitle.text.toString()
            val subtitle = etSubtitle.text.toString()
            val taste = etTaste.text.toString()
            val details = etDetails.text.toString()
            val body = sliderBody.value.toInt()
            val aroma = sliderAroma.value.toInt()
            val caffeine = sliderCaffeine.value.toInt()

            Bean(
                null, title, subtitle, taste, details, body, aroma, caffeine, ""
            )
        }
    }
}