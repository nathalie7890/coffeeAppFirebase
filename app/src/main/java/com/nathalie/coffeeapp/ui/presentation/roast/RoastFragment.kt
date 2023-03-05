package com.nathalie.coffeeapp.ui.presentation.roast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nathalie.coffeeapp.MyApplication
import com.nathalie.coffeeapp.adapters.RoastAdapter
import com.nathalie.coffeeapp.databinding.FragmentRoastBinding
import com.nathalie.coffeeapp.ui.presentation.MainFragment
import com.nathalie.coffeeapp.ui.presentation.MainFragmentDirections
import com.nathalie.coffeeapp.viewmodels.MainViewModel
import com.nathalie.coffeeapp.viewmodels.roast.RoastViewModel

class RoastFragment : Fragment() {
    private lateinit var binding: FragmentRoastBinding
    private lateinit var adapter: RoastAdapter
    private val viewModel: RoastViewModel by viewModels {
        RoastViewModel.Provider((requireActivity().applicationContext as MyApplication).roastRepo)
    }
    private val parentViewModel: MainViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRoastBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        (requireParentFragment() as MainFragment).sayHello()

        viewModel.roasts.observe(viewLifecycleOwner) {
            adapter.setRoasts(it)
            //if no item, display this
            binding.emptyRoast?.isVisible = adapter.itemCount <= 0
        }

        //when refresh is done, hide the refesh icon
        viewModel.swipeRefreshLayoutFinished.asLiveData()
            .observe(viewLifecycleOwner) {
                binding.srlRefresh.isRefreshing = false
            }

        parentViewModel.refreshRoast.observe(viewLifecycleOwner) {
            if (it) {
                refresh()
                parentViewModel.shouldRefreshRoast(false)
            }
        }

        binding.run {
            //when swipe down to refresh, refresh this fragment
            srlRefresh.setOnRefreshListener {
                viewModel.onRefresh()
            }

            //when add roast level btn is clicked, take user to add roast fragment
            binding.btnAddRoast.setOnClickListener {
                val action = MainFragmentDirections.actionMainToAddRoast()
                NavHostFragment.findNavController(this@RoastFragment).navigate(action)
            }
        }
    }

    //fetch roasts
    fun refresh() {
        viewModel.getRoasts()
    }

    fun setupAdapter() {
        val orientation = resources.configuration.orientation
        val layoutManager: LinearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = RoastAdapter(emptyList()) {
            val action = MainFragmentDirections.actionMainToEditRoast(it)
            NavHostFragment.findNavController(this).navigate(action)
        }
        binding.rvRoast.adapter = adapter
        binding.rvRoast.layoutManager = layoutManager
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