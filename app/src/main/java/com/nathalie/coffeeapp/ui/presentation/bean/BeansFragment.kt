package com.nathalie.coffeeapp.ui.presentation.bean

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.databinding.FragmentBeansBinding
import com.nathalie.coffeeapp.ui.presentation.BaseFragment
import com.nathalie.coffeeapp.viewmodels.MainViewModel
import com.nathalie.coffeeapp.ui.adapter.BeanAdapter
import com.nathalie.coffeeapp.ui.presentation.MainFragmentDirections
import com.nathalie.coffeeapp.ui.viewmodels.bean.BeanViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
// Fragment bound to the Beans UI
class BeansFragment : BaseFragment<FragmentBeansBinding>() {
    private lateinit var adapter: BeanAdapter
    private val parentViewModel: MainViewModel by viewModels(ownerProducer = { requireParentFragment() })
    override val viewModel: BeanViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_beans

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            // fetches all the beans
            viewModel.getBeans()
        }
    }

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        setupAdapter()

        binding?.run {
            // Goes to the add bean fragment
            btnAddBean.setOnClickListener {
                val action = MainFragmentDirections.actionMainToAddBean()
                navController.navigate(action)
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        viewModel.beans.observe(viewLifecycleOwner) {
            // adds the fetched list of beans to the beans adapter
            adapter.setBeans(it.toMutableList())

            binding?.emptyBean?.isVisible = adapter.itemCount <= 0
            if(it.isNotEmpty()) {
                binding?.llLoader?.isVisible = false
            }
        }

//        parentViewModel.refreshBeans.observe(viewLifecycleOwner) {
//            lifecycleScope.launch {
//                viewModel.getBeans()
//            }
//            parentViewModel.shouldRefreshDrinks(false)
//        }
    }

    // Binds the recycler view and the data, sets the click listener to navigate to details
    fun setupAdapter() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = BeanAdapter(mutableListOf())
        adapter.listener = object : BeanAdapter.Listener {
            override fun onClick(item: Bean) {
                val action = item.id?.let { MainFragmentDirections.actionMainToDetailBean(it) }
                if (action != null) {
                    NavHostFragment.findNavController(this@BeansFragment).navigate(action)
                }
            }
        }

        binding?.rvBeans?.adapter = adapter
        binding?.rvBeans?.layoutManager = layoutManager
    }

    companion object {
        private var beansFragmentInstance: BeansFragment? = null
        fun getInstance(): BeansFragment {
            if (beansFragmentInstance == null) {
                beansFragmentInstance = BeansFragment()
            }

            return beansFragmentInstance!!
        }
    }
}