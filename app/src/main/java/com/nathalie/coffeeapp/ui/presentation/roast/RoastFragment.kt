package com.nathalie.coffeeapp.ui.presentation.roast

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.databinding.FragmentRoastBinding
import com.nathalie.coffeeapp.ui.adapter.RoastAdapter
import com.nathalie.coffeeapp.ui.presentation.BaseFragment
import com.nathalie.coffeeapp.ui.presentation.MainFragmentDirections
import com.nathalie.coffeeapp.viewmodels.MainViewModel
import com.nathalie.coffeeapp.ui.viewmodels.roast.RoastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
// Fragment bound to the Roast UI
class RoastFragment : BaseFragment<FragmentRoastBinding>() {
    private lateinit var adapter: RoastAdapter

    //    private val viewModel2: RoastViewModel by viewModels {
//        RoastViewModel.Provider((requireActivity().applicationContext as MyApplication).roastRepo)
//    }
    private val parentViewModel: MainViewModel by viewModels(ownerProducer = { requireParentFragment() })
    override val viewModel: RoastViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_roast


    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        setupAdapter()

        lifecycleScope.launch {
            viewModel.swipeRefreshLayoutFinished.collect {
                // refreshes and refetches when screen is swiped down
                binding?.srlRefresh?.isRefreshing = false
            }
        }

        binding?.run {
            // refreshes and refetches when screen is swiped down
            srlRefresh.setOnRefreshListener {
                viewModel.onRefresh()
            }

            // navigate to add roast fragment
            btnAddRoast.setOnClickListener {
                val action = MainFragmentDirections.actionMainToAddRoast()
                navController.navigate(action)
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        viewModel.roasts.observe(viewLifecycleOwner) {
            // adds the fetched list of roasts to the roast adapter
            adapter.setRoasts(it.toMutableList())
            //if no item, display this
            binding?.emptyRoast?.isVisible = adapter.itemCount <= 0
        }

        // refresh function to refetch the roasts
        parentViewModel.refreshRoast.observe(viewLifecycleOwner) {
            if (it) {
                refresh()
                parentViewModel.shouldRefreshRoast(false)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            // fetches all the roasts
            viewModel.getRoasts()
        }
    }

    //fetch roasts
    fun refresh() {
        viewModel.onRefresh()
    }

    // Binds the recycler view and the data, sets the click listener to navigate to edit
    fun setupAdapter() {
        val orientation = resources.configuration.orientation
        val layoutManager: LinearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = RoastAdapter(mutableListOf())
        adapter.listener = object : RoastAdapter.Listener {
            override fun onClick(id: String) {
                val action = MainFragmentDirections.actionMainToEditRoast(id)
                navController.navigate(action)
            }
        }
        binding?.rvRoast?.adapter = adapter
        binding?.rvRoast?.layoutManager = layoutManager
    }

    companion object {
        private var roastFragmentInstance: RoastFragment? = null
        fun getInstance(): RoastFragment {
            if (roastFragmentInstance == null) {
                roastFragmentInstance = RoastFragment()
            }

            return roastFragmentInstance!!
        }
    }
}