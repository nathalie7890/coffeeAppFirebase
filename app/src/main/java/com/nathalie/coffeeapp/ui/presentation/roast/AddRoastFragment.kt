package com.nathalie.coffeeapp.ui.presentation.roast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.ui.utils.Utils.showSnackbar
import com.nathalie.coffeeapp.ui.viewmodels.roast.AddRoastViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.nathalie.coffeeapp.data.model.fireStoreModel.Roast

@AndroidEntryPoint
class AddRoastFragment : BaseRoastFragment() {
    override val viewModel: AddRoastViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_add_roast

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        binding?.run {
            btnAdd.setOnClickListener {
                val title = etTitle.text.toString()
                val details = etDetails.text.toString()

                if (title.isNotEmpty() && details.isNotEmpty()) {
                    val roast = Roast(null, title, details, "", "")
                    viewModel.addRoast(roast)
                    val bundle = Bundle()
                    bundle.putBoolean("refresh", true)
                    setFragmentResult("from_add_roast", bundle)
                    navController.popBackStack()
                    showSnackbar(requireView(), requireContext(), "$title added to Roast Levels")
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
}