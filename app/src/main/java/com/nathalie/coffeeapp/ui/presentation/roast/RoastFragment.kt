package com.nathalie.coffeeapp.ui.presentation.roast

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.databinding.FragmentRoastBinding
import com.nathalie.coffeeapp.ui.adapter.RoastAdapter
import com.nathalie.coffeeapp.ui.presentation.BaseFragment
import com.nathalie.coffeeapp.ui.presentation.MainFragmentDirections
import com.nathalie.coffeeapp.viewmodels.MainViewModel
import com.nathalie.coffeeapp.ui.viewmodels.roast.RoastViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        viewModel.swipeRefreshLayoutFinished.asLiveData().observe(viewLifecycleOwner) {
            binding?.srlRefresh?.isRefreshing = false
        }

        binding?.run {
            srlRefresh.setOnRefreshListener {
                viewModel.onRefresh()
            }
            btnAddRoast.setOnClickListener {
                val action = MainFragmentDirections.actionMainToAddRoast()
                navController.navigate(action)
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        viewModel.roasts.observe(viewLifecycleOwner) {
            adapter.setRoasts(it.toMutableList())
            //if no item, display this
            binding?.emptyRoast?.isVisible = adapter.itemCount <= 0
        }

        parentViewModel.refreshRoast.observe(viewLifecycleOwner) {
            if (it) {
                refresh()
                parentViewModel.shouldRefreshRoast(false)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRoasts()
//        setupAdapter()

//        (requireParentFragment() as MainFragment).sayHello()


        //when refresh is done, hide the refresh icon
//        viewModel.swipeRefreshLayoutFinished.asLiveData()
//            .observe(viewLifecycleOwner) {
//                binding.srlRefresh.isRefreshing = false
//            }

//        parentViewModel.refreshRoast.observe(viewLifecycleOwner) {
//            if (it) {
//                refresh()
//                parentViewModel.shouldRefreshRoast(false)
//            }
//        }

//        binding.run {
//            //when swipe down to refresh, refresh this fragment
//            srlRefresh.setOnRefreshListener {
//                viewModel.onRefresh()
//            }

        //when add roast level btn is clicked, take user to add roast fragment
//        binding.btnAddRoast.setOnClickListener {
//            val action = MainFragmentDirections.actionMainToAddRoast()
//            NavHostFragment.findNavController(this@RoastFragment).navigate(action)
//        }
    }

    //fetch roasts
    fun refresh() {
        viewModel.getRoasts()
    }

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